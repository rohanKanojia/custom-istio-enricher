/**
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package io.fabric8.maven.sample.enricher.istio;

import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.maven.core.util.Configs;
import io.fabric8.maven.core.util.MavenUtil;
import io.fabric8.maven.enricher.api.BaseEnricher;
import io.fabric8.maven.enricher.api.MavenEnricherContext;
import me.snowdrop.istio.api.networking.v1alpha3.GatewayBuilder;
import io.fabric8.maven.core.config.PlatformMode;

/**
 * @author roland
 * @since 10.04.17
 */
public class IstioEnricher extends BaseEnricher {

    public IstioEnricher(MavenEnricherContext buildContext) {
        super(buildContext, "istio-enricher");
    }

    // Available configuration keys
    private enum Config implements Configs.Key {
        // name of the secret to create
        name,

        // Whether to do base64 encoding
        encode {{ d = "true"; }};

        public String def() { return d; } protected String d;
    }

    @Override
    public void enrich(PlatformMode platformMode, KubernetesListBuilder builder) {
        log.info(" Istio Enricher: Added dummy Gateway");
        builder.addToItems(createGatewayBuilder());
    }

    private GatewayBuilder createGatewayBuilder() {
        return  new GatewayBuilder()
                .withNewMetadata()
                .withName(getGatewayName())
                .endMetadata()
                .withNewSpec()
                .addToSelector("app", "test-app")
                .addNewServer()
                .withNewPort()
                .withNumber(80)
                .withName("http")
                .withProtocol("HTTP")
                .endPort()
                .addNewHost("uk.bookinfo.com")
                .addNewHost("in.bookinfo.com")
                .withNewTls()
                .withHttpsRedirect(true)
                .endTls()
                .endServer()
                .endSpec();
    }

    private String getGatewayName() {
        return getConfig(Config.name,
                         MavenUtil.createDefaultResourceName("custom-enricher"));
    }

    private boolean isTypedKey(String key) {
        for (Config cfg : Config.values()) {
            if (cfg.name().equals(key)) {
                return true;
            }
        }
        return false;
    }
}
