package repository;

import io.micronaut.configuration.arango.ArangoClientAsync;
import model.dao.Positions;
import sx.microservices.framework.config.context.RepositoryContext;
import sx.microservices.framework.repository.DocumentRepository;
import sx.microservices.registry.commission.model.dao.CommissionPositions;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PositionsRepository extends DocumentRepository<Positions> {

    @Inject
    public PositionsRepository(ArangoClientAsync client,
                               RepositoryContext context) {
        super(client, context, Positions.class);
    }
}
