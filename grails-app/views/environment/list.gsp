
<%@ page import="esbmon.Environment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'stage.label', default: 'Environment')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'stage.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'stage.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="suffix" title="${message(code: 'stage.suffix.label', default: 'Suffix')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${stageInstanceList}" status="i" var="stageInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${stageInstance.id}">${fieldValue(bean: stageInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: stageInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: stageInstance, field: "suffix")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${stageInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
