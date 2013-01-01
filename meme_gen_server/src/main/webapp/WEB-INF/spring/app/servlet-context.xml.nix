<?xml version="1.0" encoding="UTF-8"?>
<beans:beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:beans="http://www.springframework.org/schema/beans"
    xmlns="http://www.springframework.org/schema/mvc"
    xsi:schemaLocation="
        http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd" >

    <!-- DispatcherServlet Context: defines this servlet's request-processing infrastructure -->


    <!-- Enables the Spring MVC @Controller programming model -->

    <annotation-driven />

    <!-- Handles HTTP GET requests for /resources/** by efficiently serving up static resources in the ${webappRoot}/resources directory -->

    <resources
        location="/resources/"
        mapping="/resources/**" />

    <!-- Resolves views selected for rendering by @Controllers to .jsp resources in the /WEB-INF/views directory -->

    <beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" >

        <beans:property
            name="prefix"
            value="/WEB-INF/views/" />

        <beans:property
            name="suffix"
            value=".jsp" />
    </beans:bean>

    <!-- Imports user-defined @Controller beans that process client requests -->

    <beans:bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter" >
        <!--
            NOTE: it is important to specify the order property, so this
            adapter will be attempted before the HandlerAdapter that
            Spring MVC automatically configures. This is because a
            MappingJacksonHttpMessageConverter is registered
            automatically with the default adapter that will attempt to
            handle any Java object including BufferedImage.
        -->

        <beans:property
            name="order"
            value="1" />

        <beans:property name="messageConverters" >
            <beans:list>
                <!-- Default converters -->

                <beans:bean class="org.springframework.http.converter.StringHttpMessageConverter" />

                <beans:bean class="org.springframework.http.converter.FormHttpMessageConverter" />

                <beans:bean class="org.springframework.http.converter.ByteArrayHttpMessageConverter" >
                    <beans:property name="supportedMediaTypes" >
                        <beans:list>
                            <beans:value>
                                image/jpeg
                            </beans:value>
                        </beans:list>
                    </beans:property>
                </beans:bean>

                <beans:bean class="org.springframework.http.converter.xml.SourceHttpMessageConverter" />

                <!-- Converter for images -->
                <beans:bean class="org.springframework.http.converter.BufferedImageHttpMessageConverter" />

                <!-- This must come after our image converter -->
                <beans:bean class="org.springframework.http.converter.json.MappingJacksonHttpMessageConverter" />
            </beans:list>
        </beans:property>
    </beans:bean>
    
    <beans:bean name="memeImagesRootDir" class="java.lang.String">
        <beans:constructor-arg value="C:/meme_imgs"/>
    </beans:bean>
    
    
    <beans:import resource="controllers.xml" />

</beans:beans>