package org.mule.extension.splunk.internal;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations({MuleSplunkLoggerOperations.class})
//@ConnectionProviders(MuleSplunkLoggerConnectionProvider.class)
public class MuleSplunkLoggerConfiguration {

  @Parameter
  private String splunkUrl;

  @Parameter
  private String authToken;

  @DisplayName("Trust Store Configuration")
  @Parameter
  private SplunkTrustStoreConfigDto splunkTsConfigDto;

/*  public MuleSplunkLoggerConfiguration(String splunkUrl, String authToken, SplunkTrustStoreConfigDto splunkTsConfigDto) {
    this.splunkUrl = splunkUrl;
    this.authToken = authToken;
    this.splunkTsConfigDto = splunkTsConfigDto;
  }*/

  public SplunkTrustStoreConfigDto getSplunkTsConfigDto() {
    return splunkTsConfigDto;
  }

/*  @Parameter
  private String trustStoreLocation;

  @Parameter
  private String trustStorePassword;*/

/*  public MuleSplunkLoggerConfiguration(String splunkUrl, String authToken, String trustStoreLocation, String trustStorePassword) {
    this.splunkUrl = splunkUrl;
    this.authToken = authToken;
    this.trustStoreLocation = trustStoreLocation;
    this.trustStorePassword = trustStorePassword;
  }*/

/*  public String getTrustStoreLocation() {
    return trustStoreLocation;
  }

  public String getTrustStorePassword() {
    return trustStorePassword;
  }*/

  public String getSplunkUrl() {
    return splunkUrl;
  }

  public String getAuthToken() {
    return authToken;
  }
}
