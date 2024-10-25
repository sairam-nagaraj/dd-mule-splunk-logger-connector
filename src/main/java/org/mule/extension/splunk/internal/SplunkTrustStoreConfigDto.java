package org.mule.extension.splunk.internal;

import org.mule.runtime.extension.api.annotation.param.Parameter;

public class SplunkTrustStoreConfigDto {

    @Parameter
    private String trustStoreLocation;

    @Parameter
    private String trustStorePassword;

    public String getTrustStoreLocation() {
        return trustStoreLocation;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }
}
