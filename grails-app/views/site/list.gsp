
<%@ page import="esbmon.Site" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'site.label', default: 'Site')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'site.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="domainName" title="${message(code: 'site.domainName.label', default: 'Domain Name')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'site.name.label', default: 'Name')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${siteInstanceList}" status="i" var="siteInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${siteInstance.id}">${fieldValue(bean: siteInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: siteInstance, field: "domainName")}</td>
                        
                            <td>${fieldValue(bean: siteInstance, field: "name")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${siteInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
