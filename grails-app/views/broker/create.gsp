<%@ page import="esbmon.Broker" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName"
         value="${message(code: 'broker.label', default: 'Broker')}"/>
  <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<div class="nav">
  <span class="menuButton"><a class="home"
                              href="${createLink(uri: '/')}"><g:message
            code="default.home.label"/></a></span>
  <span class="menuButton"><g:link class="list" action="list"><g:message
          code="default.list.label" args="[entityName]"/></g:link></span>
</div>

<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${brokerInstance}">
    <div class="errors">
      <g:renderErrors bean="${brokerInstance}" as="list"/>
    </div>
  </g:hasErrors>
  <g:form action="save">
    <div class="dialog">
      <table>
        <tbody>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="node"><g:message code="broker.node.label"
                                         default="Node"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: brokerInstance, field: 'node', 'errors')}">
            <g:select name="node.id" from="${esbmon.Node.list()}" optionKey="id"
                      value="${brokerInstance?.node?.id}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="environment"><g:message code="broker.environment.label"
                                                default="Environment"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: brokerInstance, field: 'environment', 'errors')}">
            <g:select name="environment.id" from="${esbmon.Environment.list()}"
                      optionKey="id"
                      value="${brokerInstance?.environment?.id}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="site"><g:message code="broker.site.label"
                                         default="Site"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: brokerInstance, field: 'site', 'errors')}">
            <g:select name="site.id" from="${esbmon.Site.list()}" optionKey="id"
                      value="${brokerInstance?.site?.id}"/>
          </td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="buttons">
      <span class="button"><g:submitButton name="create" class="save"
                                           value="${message(code: 'default.button.create.label', default: 'Create')}"/></span>
    </div>
  </g:form>
</div>
</body>
</html>
