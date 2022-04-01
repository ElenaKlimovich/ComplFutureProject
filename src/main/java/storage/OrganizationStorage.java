package storage;

import model.dao.Organization;
import repository.OrganizationRepository;
import sx.microservices.framework.config.context.StorageContext;
import sx.microservices.framework.storage.DocumentStorage;
import sx.microservices.registry.handlers.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class OrganizationStorage extends DocumentStorage<Organization> {

    @Inject
    public OrganizationStorage(OrganizationRepository repository,
                               List<Handler> handlers,
                               StorageContext context) {
        super(repository, handlers, context);
    }
}
