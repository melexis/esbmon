<%@ page import="esbmon.Broker" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName"
         value="${message(code: 'broker.label', default: 'Broker')}"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
  <sparklines:resources />
</head>

<body>
<div class="nav">
  <span class="menuButton"><a class="home"
                              href="${createLink(uri: '/')}"><g:message
            code="default.home.label"/></a></span>
  <span class="menuButton"><g:link class="create" action="index"><g:message
          code="broker.info.refresh" args="[entityName]"/></g:link></span>
</div>

<div class="body">
  <h1><g:message code="default.list.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="list">
    <table>
      <thead>
      <tr>

        <g:sortableColumn property="id"
                          title="${message(code: 'broker.id.label', default: 'Id')}"/>

        <th><g:message code="broker.node.label" default="Node"/></th>

        <th><g:message code="broker.environment.label"
                       default="Environment"/></th>

        <th><g:message code="broker.site.label" default="Site"/></th>

        <th><g:message code="broker.info.heapMemory" default="Site"/></th>

        <th><g:message code="broker.info.nonHeapMemory" default="Site"/></th>

      </tr>
      </thead>
      <tbody>
      <g:each in="${brokerInstanceList}" status="i" var="brokerInstance">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <td><g:link action="show"
                      id="${brokerInstance.id}">${fieldValue(bean: brokerInstance, field: "id")}</g:link></td>

          <td>${fieldValue(bean: brokerInstance, field: "node")}</td>

          <td>${fieldValue(bean: brokerInstance, field: "environment")}</td>

          <td>${fieldValue(bean: brokerInstance, field: "site")}</td>

          <td><sparklines:pie id="heap-${brokerInstance.id}"
                              values="${heapInfo[brokerInstance]}"/>
            ${heapInfo[brokerInstance][0]}
          </td>

          <td>
            <sparklines:pie id="non-heap-${brokerInstance.id}"
                              values="${nonHeapInfo[brokerInstance]}"/>
            ${nonHeapInfo[brokerInstance][0]}
          </td>

        </tr>
      </g:each>
      </tbody>
    </table>
  </div>

  <div class="paginateButtons">
    <g:paginate total="${brokerInstanceTotal}"/>
  </div>
</div>
</body>
</html>
