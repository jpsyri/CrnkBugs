Test cases for few bugs in [Crnk](https://github.com/crnk-project/crnk-framework) related to inheritance on resources
========================================================================================================================

Tests cases can be executed with
    
    ./gradlew test
    
Inherited relation problem
--------------------------

In case resource derived from parent resource contains relation, links for those relations are included in the JSON 
responses for those resources. However requests to those links result in exception ('related-resource-b' is name of 
the relation in the derived resource class)

    io.crnk.core.exception.BadRequestException: field not found: related-resource-b
    at io.crnk.core.engine.internal.dispatcher.path.PathBuilder.parseFieldPath(PathBuilder.java:154)
    at io.crnk.core.engine.internal.dispatcher.path.PathBuilder.parseIdPath(PathBuilder.java:119)
    at io.crnk.core.engine.internal.dispatcher.path.PathBuilder.parseResourcePath(PathBuilder.java:113)
    at io.crnk.core.engine.internal.dispatcher.path.PathBuilder.build(PathBuilder.java:97)
    at io.crnk.core.engine.internal.http.JsonApiRequestProcessorBase.getJsonPath(JsonApiRequestProcessorBase.java:104)
    at io.crnk.core.engine.internal.http.JsonApiRequestProcessor.accepts(JsonApiRequestProcessor.java:85)
    at io.crnk.core.engine.internal.http.HttpRequestDispatcherImpl.process(HttpRequestDispatcherImpl.java:73)
    at io.crnk.rs.CrnkFilter.filter(CrnkFilter.java:49)
        ...
 
The problem is demonstrated with 'testInheritedRelation' test at [CrnkTestApplicationTest.java](src/test/java/net/kirnu/crnk/CrnkTestApplicationTest.java)


Cyclic reference problem
------------------------

In this case we have following setup: 

   - CyclicResourceA contains relation to CyclicResourceC (and vice versa)
   - CyclicResourceASub1 derived from CyclicResourceA contains relation to CyclicResourceB (and vice versa)

Now trying to create repository for CyclicResourceA from CrnkClient results an exception:

    java.lang.IllegalStateException: resourceType 'resource-a' already exists, cannot add entry io.crnk.core.engine.internal.registry.LegacyRegistryEntry@52bd9a27
        at io.crnk.core.engine.internal.utils.PreconditionUtil.fail(PreconditionUtil.java:50)
        at io.crnk.core.engine.internal.utils.PreconditionUtil.verify(PreconditionUtil.java:131)
        at io.crnk.core.engine.registry.DefaultResourceRegistryPart.addEntry(DefaultResourceRegistryPart.java:47)
        at io.crnk.core.engine.internal.registry.ResourceRegistryImpl.addEntry(ResourceRegistryImpl.java:215)
        at io.crnk.core.engine.internal.registry.ResourceRegistryImpl.addEntry(ResourceRegistryImpl.java:54)
        at io.crnk.client.CrnkClient.allocateRepository(CrnkClient.java:387)
        at io.crnk.client.CrnkClient.allocateRepositoryRelation(CrnkClient.java:419)
        at io.crnk.client.CrnkClient.allocateRepositoryRelations(CrnkClient.java:408)
        at io.crnk.client.CrnkClient.allocateRepository(CrnkClient.java:389)
        at io.crnk.client.CrnkClient.allocateRepositoryRelation(CrnkClient.java:419)
        at io.crnk.client.CrnkClient.allocateRepositoryRelations(CrnkClient.java:408)
        at io.crnk.client.CrnkClient.allocateRepository(CrnkClient.java:389)
        at io.crnk.client.CrnkClient.allocateRepositoryRelation(CrnkClient.java:419)
        at io.crnk.client.CrnkClient.allocateRepositoryRelations(CrnkClient.java:408)
        at io.crnk.client.CrnkClient.allocateRepository(CrnkClient.java:389)
        at io.crnk.client.CrnkClient.access$400(CrnkClient.java:95)
        at io.crnk.client.CrnkClient$ClientResourceRegistry.findEntry(CrnkClient.java:648)
        at io.crnk.core.engine.internal.registry.ResourceRegistryImpl.findEntry(ResourceRegistryImpl.java:75)
        at io.crnk.client.CrnkClient.getRepositoryForType(CrnkClient.java:486)
        at net.kirnu.crnk.CrnkTestApplicationTest.relationQueryTest(CrnkTestApplicationTest.java:57)
            ...   

The problem is demonstrated with 'testCyclicRelationsRepository' test at [CrnkTestApplicationTest.java](src/test/java/net/kirnu/crnk/CrnkTestApplicationTest.java)
