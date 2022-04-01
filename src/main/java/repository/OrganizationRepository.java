package repository;

import io.micronaut.configuration.arango.ArangoClientAsync;
import model.dao.Organization;
import sx.microservices.framework.config.context.RepositoryContext;
import sx.microservices.framework.repository.DocumentRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OrganizationRepository extends DocumentRepository<Organization> {

    @Inject
    public OrganizationRepository(ArangoClientAsync client,
                                  RepositoryContext context) {
        super(client, context, Organization.class);
    }

}
