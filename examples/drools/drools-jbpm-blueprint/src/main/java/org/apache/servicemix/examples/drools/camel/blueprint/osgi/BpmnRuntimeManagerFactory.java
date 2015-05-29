package org.apache.servicemix.examples.drools.camel.blueprint.osgi;

import javax.persistence.EntityManagerFactory;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.drools.persistence.jta.JtaTransactionManager;
import org.jbpm.process.core.timer.impl.ThreadPoolSchedulerService;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 */
public class BpmnRuntimeManagerFactory {
    /**
     * Logger
     */
    private static final Logger log = LoggerFactory
            .getLogger(BpmnRuntimeManagerFactory.class);

    /**
     * EntityManagerFactoryManager.
     */
    private EntityManagerFactory emf;

    /**
     * 
     * @param emf
     * @param tm
     * @param ut
     */
    public BpmnRuntimeManagerFactory(EntityManagerFactory emf,
            TransactionManager tm, UserTransaction ut) {
        this.emf = emf;
        this.tm = tm;
        this.ut = ut;
    }

    /**
     * TransactionManager.
     */
    private TransactionManager tm;
    /**
     * 
     */
    private UserTransaction ut;

    /**
     * @param ut
     *            the ut to set
     */
    public void setUt(UserTransaction ut) {
        this.ut = ut;
    }

    /**
     * @param tm
     *            the tm to set
     */
    public void setTm(TransactionManager tm) {
        this.tm = tm;
    }

    /**
     * 
     */
    private RuntimeEngine engine;

    /**
     * 
     */
    private RuntimeEnvironment environment;

    /**
     * 
     */
    private RuntimeManager manager;

    /**
     * 
     */
    private ThreadPoolSchedulerService tservice = new ThreadPoolSchedulerService(
            10);

    /**
     * destroy
     */
    public void destroy() {
        tservice.shutdown();

        if (engine != null) {
            manager.disposeRuntimeEngine(engine);
            engine = null;
        }

        if (null != manager) {
            manager.close();
            manager = null;
        }
    }

    /**
     * @return the engine
     */
    public RuntimeEngine getEngine() {
        return engine;
    }

    /**
     * init.
     * 
     * @throws SystemException
     * @throws NotSupportedException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws RollbackException
     * @throws IllegalStateException
     * @throws SecurityException
     */
    public void init() throws NotSupportedException, SystemException,
            SecurityException, IllegalStateException, RollbackException,
            HeuristicMixedException, HeuristicRollbackException {

        ClassLoader originalClassLoader = null;
        tm.begin();
        try {
            originalClassLoader = Thread.currentThread()
                    .getContextClassLoader();
            ClassLoader cl = BpmnRuntimeManagerFactory.class.getClassLoader();

            Thread.currentThread().setContextClassLoader(cl);

            RuntimeEnvironmentBuilder builder = null;

            builder = RuntimeEnvironmentBuilder.Factory.get()
                    .newDefaultBuilder();

            builder.classLoader(cl);

            builder.addAsset(ResourceFactory.newClassPathResource(
                    "bpmn/SimpleProcess.bpmn2",
                    BpmnRuntimeManagerFactory.class.getClassLoader()),
                    ResourceType.BPMN2);

            builder.addEnvironmentEntry(EnvironmentName.TRANSACTION, ut);
            builder.addEnvironmentEntry(EnvironmentName.TRANSACTION_MANAGER, tm);

            builder.schedulerService(tservice);
            builder.entityManagerFactory(emf);

            environment = builder.get();

            manager = RuntimeManagerFactory.Factory.get()
                    .newSingletonRuntimeManager(environment,
                            "bpmn-process-example");

            engine = manager.getRuntimeEngine(EmptyContext.get());
        } catch (RuntimeException e) {
            log.error("Error", e.getCause());
            if (tm.getStatus() == Status.STATUS_ACTIVE) {
                tm.rollback();
            }
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
            if (tm.getStatus() == Status.STATUS_ACTIVE) {
                tm.commit();
            }
        }

    }

    /**
     * RuntimeManager.
     * 
     * @return
     */
    public RuntimeManager manager() {
        return manager;
    }

    /**
     * 
     * @return
     */
    public KieSession kieSession() {
        return engine.getKieSession();
    }

    /**
     * @param emf
     *            the emf to set
     */
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
