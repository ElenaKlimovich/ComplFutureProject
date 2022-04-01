package graphql;

import graphql.schema.DataFetchingEnvironment;
import model.dao.Organization;
import model.dao.Positions;
import storage.OrganizationStorage;
import storage.PositionsStorage;
import sx.microservices.framework.error.ParameterException;
import sx.microservices.framework.storage.query.filter.Rsql;
import sx.microservices.management.tracing.TraceContext;
import sx.microservices.registry.handlers.model.Field;
import sx.microservices.registry.handlers.security.UserContext;
import sx.microservices.registry.module.graphql.api.config.context.FetcherContext;
import sx.microservices.registry.module.graphql.api.model.QueryQL;
import sx.microservices.registry.module.graphql.api.model.typedefinition.GraphQLType;
import sx.microservices.registry.module.graphql.api.model.typedefinition.TypeField;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Запрос на получение количества вакантных мест
 * Для executionType = 1 (постоянная должность) вернет positionVacantCount,
 * для executionType = 2 (временная должность) вернет positionTemporaryVacantCount
 **/
@Singleton
public class PositionsCountFetcher //extends AbstractDocumentFetcher<CompletableFuture<Integer>>
{

    private static final String ORIGIN = Positions.class.getSimpleName();
    private final PositionsStorage positionsStorage;
    private final OrganizationStorage orgStorage;

    @Inject
    public PositionsCountFetcher(FetcherContext context,
                                 PositionsStorage positionsStorage,
                                 OrganizationStorage orgStorage) {
//        super(context);
        this.positionsStorage = positionsStorage;
        this.orgStorage = orgStorage;
    }

    public static final String ORG_ID = "orgId";
    public static final String EXECUTION_TYPE = "executionType";
    public static final String POSITION_CODE = "positionCode";
    public static final String ENABLE = "enable";

    @Override
    public QueryQL query() {
        return QueryQL.query("get" + ORIGIN + "Count",
                ORIGIN,
                List.of(TypeField.required(GraphQLType.ID, ORG_ID, "Идентификатор организации"),
                        TypeField.required(GraphQLType.STRING, EXECUTION_TYPE, "Тип должности (постоянная или временная)"),
                        TypeField.required(GraphQLType.STRING, POSITION_CODE, "Код должности")),
                TypeField.required(GraphQLType.INT),
                "Количество вакантных должностей");
    }

    @TraceContext("graphql_Positions_count")
    @Override
    public CompletableFuture<Integer> get(DataFetchingEnvironment environment,
                                          UserContext context) throws Exception {
        final String orgId = environment.getArgument(ORG_ID);
        final String executionType = environment.getArgument(EXECUTION_TYPE);
        final String positionCode = environment.getArgument(POSITION_CODE);

        orgStorage.find(orgId, null, Field.include(Organization.LINK_POSITION), context)
                .thenCompose(org -> {
                    if (org.isPresent()) {
                        List<String> commissionPositionsIds = (List<String>) org.get().getExpands().get(Organization.LINK_POSITION);
                        if (commissionPositionsIds != null) {
                            positionsStorage.search(Rsql
                                            .ofField("id").in(commissionPositionsIds)
                                            .and()
                                            .field(ENABLE).equal("true")
                                            .and()
                                            .field(POSITION_CODE).equal(positionCode)
                                            .build(), context)
                                    .thenApply(positions -> {
                                        Integer result;
                                        if (positions.size() > 1)
                                            throw new ParameterException("More than 1 CommissionPositions found!");
                                        else if (positions.isEmpty())
                                            throw new ParameterException("No records found!");
                                        else {
                                            if (executionType.equals("1")) {
                                                result = positions.get(0).getPositionVacantCount();
                                            } else if (executionType.equals("2")) {
                                                result = positions.get(0).getPositionTemporaryVacantCount();
                                            } else
                                                throw new ParameterException("executionType must equals 1 or 2 !");
                                        }
                                        return result;
                                    });
                        }
                    }
                    return null;
                });
        return null;
    }
}
