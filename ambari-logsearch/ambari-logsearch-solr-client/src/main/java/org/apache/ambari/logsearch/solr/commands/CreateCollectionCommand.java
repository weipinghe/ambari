/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ambari.logsearch.solr.commands;

import org.apache.ambari.logsearch.solr.AmbariSolrCloudClient;
import org.apache.ambari.logsearch.solr.util.ShardUtils;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;

import java.util.ArrayList;
import java.util.List;

public class CreateCollectionCommand extends AbstractSolrRetryCommand<CollectionAdminRequest.Create ,String> {

  public CreateCollectionCommand(int maxRetries, int interval) {
    super(maxRetries, interval);
  }

  @Override
  public String handleResponse(CollectionAdminResponse response, AmbariSolrCloudClient client) throws Exception {
    return client.getCollection();
  }

  @Override
  public CollectionAdminRequest.Create createRequest(AmbariSolrCloudClient client) {
    CollectionAdminRequest.Create request = new CollectionAdminRequest.Create();
    request.setConfigName(client.getConfigSet());
    request.setCollectionName(client.getCollection());
    request.setNumShards(client.getShards());
    request.setReplicationFactor(client.getReplication());
    request.setMaxShardsPerNode(client.getMaxShardsPerNode());
    if (client.isSplitting()) {
      request.setShards(ShardUtils.generateShardListStr(client.getMaxShardsPerNode()));
      request.setRouterName(client.getRouterName());
      request.setRouterField(client.getRouterField());
    }
    return request;
  }

  @Override
  public String errorMessage(AmbariSolrCloudClient client) {
    return String.format("Cannot create collection: '%s'", client.getCollection());
  }
}
