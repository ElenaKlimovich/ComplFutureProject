package storage;

import model.dao.Positions;
import sx.microservices.framework.config.context.StorageContext;
import sx.microservices.framework.storage.DocumentStorage;
import sx.microservices.registry.commission.model.dao.CommissionPositions;
import sx.microservices.registry.commission.repository.CommissionPositionsRepository;
import sx.microservices.registry.handlers.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class PositionsStorage extends DocumentStorage<Positions> {

    @Inject
    public PositionsStorage(PositionsRepository repository,
                            List<Handler> handlers,
                            StorageContext context) {
        super(repository, handlers, context);
    }
}
