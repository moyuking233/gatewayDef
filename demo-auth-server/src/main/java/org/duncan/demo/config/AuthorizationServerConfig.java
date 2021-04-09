package org.duncan.demo.config;

import lombok.AllArgsConstructor;
import org.duncan.demo.component.JwtTokenEnhancer;
import org.duncan.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private JwtTokenEnhancer jwtTokenEnhancer;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Primary
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }
//    @Bean
//    public TokenStore jdbcTokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
         clients.withClientDetails(clientDetails());
//        clients.inMemory()
//                .withClient("android")
//                .scopes("read")
//                .secret(new BCryptPasswordEncoder().encode("android"))
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
//                .and()
//                .withClient("webapp")
//                .scopes("read")
//                .authorizedGrantTypes("implicit")
//                .and()
//                .withClient("browser")
//                .authorizedGrantTypes("refresh_token", "password")
//                .scopes("read");
    }
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

//    捕获异常
//    @Bean
//    public WebResponseExceptionTranslator webResponseExceptionTranslator(){
//        return new MssWebResponseExceptionTranslator();
//    }

    @Bean
    public JwtAccessTokenConverter accessTokenJwtConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }
    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
    }
    @Bean
    public TokenEnhancer customTokenEnhancer() {
        return new JwtTokenEnhancer();
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenJwtConverter());
        enhancerChain.setTokenEnhancers(delegates);
//        enhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer(), accessTokenJwtConverter()));
//        endpoints.tokenStore(new JwtTokenStore(accessTokenJwtConverter()))
        endpoints.tokenStore(tokenStore())
                .userDetailsService(userServiceImpl)
                .authenticationManager(authenticationManager);
        endpoints.accessTokenConverter(accessTokenJwtConverter());
        endpoints.tokenEnhancer(enhancerChain);

        //自定义tokenService
//        endpoints.tokenServices(defaultTokenServices());
        //认证异常翻译
//         endpoints.exceptionTranslator(webResponseExceptionTranslator());
    }

    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetails());
        // token有效期自定义设置，默认12小时
//        tokenServices.setAccessTokenValiditySeconds(60*60);
        // refresh_token默认30天
//        tokenServices.setRefreshTokenValiditySeconds(60 * 60);
        return tokenServices;
    }
}
