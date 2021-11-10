/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rabbitmq.http.client;

import com.rabbitmq.http.client.HttpLayer.HttpLayerFactory;
import org.springframework.util.Assert;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Parameters to create an instance of {@link Client}.
 *
 * @since 3.6.0
 */
public class ClientParameters {

    private URL url;
    private String username;
    private String password;
    private RestTemplateConfigurator restTemplateConfigurator;
    private HttpLayerFactory httpLayerFactory;

    /**
     * Set the URL to use.
     * <p>
     * It is not expected that the URL contains any user info.
     * Use {@link #url(String)} user info must be extracted
     * from the URL and assigned to username and password.
     *
     * @param url the URL
     * @return this client parameters instance
     */
    public ClientParameters url(URL url) {
        this.url = url;
        return this;
    }

    /**
     * Set the URL to use.
     * <p>
     * The URL can contain user info. If so, they are automatically
     * assigned to the username and password properties of this instance.
     *
     * @param url the URL
     * @return this client parameters instance
     * @throws MalformedURLException for a badly formed URL.
     */
    public ClientParameters url(String url) throws MalformedURLException {
        this.url = new URL(url);
        if (this.url.getUserInfo() != null) {
            // URL contains credentials, setting the appropriate parameters
            this.url = new URL(Utils.urlWithoutCredentials(url));
            String[] usernamePassword = Utils.extractUsernamePassword(url);
            this.username = usernamePassword[0];
            this.password = usernamePassword[1];
        }
        return this;
    }

    /**
     * Set the username to use when authenticating.
     *
     * @param username the username
     * @return this client parameters instance
     */
    public ClientParameters username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Set the password to use when authenticating.
     *
     * @param password the password
     * @return this client parameters instance
     */
    public ClientParameters password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Set a {@link RestTemplateConfigurator} to post-process the {@link Client}'s {@link org.springframework.web.client.RestTemplate}.
     * <p>
     * The default is to use {@link HttpComponentsRestTemplateConfigurator} and so using
     * {@link org.springframework.http.client.HttpComponentsClientHttpRequestFactory} in the {@link org.springframework.web.client.RestTemplate}
     * to create requests.
     *
     * @param restTemplateConfigurator the configurator to use
     * @return this client parameters instance
     * @see RestTemplateConfigurator
     * @deprecated use {@link #httpLayerFactory} instead
     */
    @Deprecated(since = "4.0.0", forRemoval = true)
    public ClientParameters restTemplateConfigurator(RestTemplateConfigurator restTemplateConfigurator) {
        this.restTemplateConfigurator = restTemplateConfigurator;
        return this;
    }

    public URL getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    /**
     *
     * @return
     * @deprecated use {@link #httpLayerFactory} instead
     */
    @Deprecated(since = "4.0.0", forRemoval = true)
    public RestTemplateConfigurator getRestTemplateConfigurator() {
        return restTemplateConfigurator;
    }

    public HttpLayerFactory getHttpLayerFactory() {
        return httpLayerFactory;
    }

    /**
     * Set the {@link HttpLayerFactory} to use.
     *
     * @param httpLayerFactory
     * @return this client parameters instance
     * @see HttpLayer#configure()
     */
    public ClientParameters httpLayerFactory(HttpLayerFactory httpLayerFactory) {
        this.httpLayerFactory = httpLayerFactory;
        return this;
    }

    void validate() {
        Assert.notNull(url, "URL is required; it must not be null");
        Assert.notNull(username, "username is required; it must not be null");
        Assert.notNull(password, "password is required; it must not be null");
    }
}
