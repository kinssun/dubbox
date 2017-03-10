/**
 * Copyright 1999-2014 dangdang.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.protocol.rest;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.http.servlet.BootstrapListener;
import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ListenerInfo;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

/**
 * TODO this impl hasn't been well tested, and we can consider move undertow to a general remoting-http impl in the future
 *
 * @author lishen
 */
public class UndertowServer extends BaseRestServer {

    private final ResteasyDeployment deployment = new ResteasyDeployment();

    private final UndertowJaxrsServer server = new UndertowJaxrsServer();

    @Override
    protected void doStart(URL url) {
        Undertow.Builder builder = Undertow.builder()
                .addHttpListener(url.getPort(), "0.0.0.0")
                .setIoThreads(url.getParameter(Constants.IO_THREADS_KEY, Constants.DEFAULT_IO_THREADS))
                .setWorkerThreads(url.getParameter(Constants.THREADS_KEY, Constants.DEFAULT_THREADS));
        server.start(builder);

//        String contextPath = "/ucopen/v1";
//        ResteasyDeployment deployment = new ResteasyDeployment();
//        deployment.setApplicationClass(UndertowApplication.class.getName());

        // Declare Servlet & Filters
//        FilterInfo springCharacterEncodingFilter = new FilterInfo("springCharacterEncodingFilter",CharacterEncodingFilter.class);
//        springCharacterEncodingFilter.setAsyncSupported(true);
//        springCharacterEncodingFilter.addInitParam("encoding", "UTF-8");
//        springCharacterEncodingFilter.addInitParam("forceEncoding", "true");

//        FilterInfo druidWebStatFilter = new FilterInfo("DruidWebStatFilter",WebStatFilter.class);
//        druidWebStatFilter.setAsyncSupported(true);
//        druidWebStatFilter.addInitParam("exclusions", "/static/*,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

//        ServletInfo dispatcherServlet = new ServletInfo("dispatcher", DispatcherServlet.class);
//        dispatcherServlet.setAsyncSupported(true);
//        dispatcherServlet.setLoadOnStartup(1);
//        dispatcherServlet.addMapping(contextPath + "/*");

//        ServletInfo druidStatViewServlet = new ServletInfo("DruidStatView", StatViewServlet.class);
//        druidStatViewServlet.setAsyncSupported(true);
//        druidStatViewServlet.addMapping("/druid/*");
//
//        FilterInfo logRecordFilter = new FilterInfo("LogRecordFilter",LogRecordFilter.class);
//        logRecordFilter.setAsyncSupported(true);

//        ServletInfo viewStatusMessagesServlet = new ServletInfo("viewStatusMessagesServlet", ViewStatusMessagesServlet.class);
//        viewStatusMessagesServlet.addMapping("/lbClassicStatus");

        // Deploy Application
        DeploymentInfo di = server.undertowDeployment(deployment);
        di.setClassLoader(Thread.currentThread().getContextClassLoader())
                .setContextPath("/")
                .setDeploymentName("dubbo-rest")
                .addListener(new ListenerInfo(BootstrapListener.class))
                .addListener(new ListenerInfo(ResteasyBootstrap.class))
//                .addListener(new ListenerInfo(LogbackConfigListener.class))
//                .addInitParameter("logbackExposeWebAppRoot", "false")
//                .addInitParameter("logbackConfigLocation", "classpath:conf/logback.xml")
//                .addInitParameter("contextConfigLocation", "classpath*:conf/application*.xml")
//                .addListener(new ListenerInfo(IntrospectorCleanupListener.class))
//                .addListener(new ListenerInfo(SpringContextLoaderListener.class))

//                .addFilter(springCharacterEncodingFilter)
//                .addFilterUrlMapping("springCharacterEncodingFilter", "/*", DispatcherType.REQUEST)
//                .addFilter(druidWebStatFilter)
//                .addFilterUrlMapping("DruidWebStatFilter", "/*", DispatcherType.REQUEST)
//                .addServlet(dispatcherServlet)
//                .addServlet(druidStatViewServlet)
//                .addFilter(logRecordFilter)
//                .addFilterUrlMapping("LogRecordFilter", "/*", DispatcherType.REQUEST)
//                .addServlet(viewStatusMessagesServlet)
//                .addWelcomePage("index.jsp")
        ;
        server.deploy(di);
    }

    @Override
    protected ResteasyDeployment getDeployment() {
        return deployment;
    }

    public void stop() {
        deployment.stop();
        server.stop();
    }
}