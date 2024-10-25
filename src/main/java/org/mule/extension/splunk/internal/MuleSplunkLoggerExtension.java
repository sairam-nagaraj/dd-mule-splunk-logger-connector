package org.mule.extension.splunk.internal;

import org.mule.runtime.api.meta.Category;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.param.Parameter;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "mule-splunk-logger")
@Extension(name = "Mule Splunk Logger", vendor = "sairam", category = Category.COMMUNITY)
@Configurations({MuleSplunkLoggerConfiguration.class})
public class MuleSplunkLoggerExtension {
}
