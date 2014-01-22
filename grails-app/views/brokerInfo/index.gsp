<%@ page import="esbmon.Broker" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <meta http-equiv="refresh" content="10">
  <g:set var="entityName"
         value="${message(code: 'broker.label', default: 'Broker')}"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
  <sparklines:resources/>
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
  <h1><g:message code="default.list.label" args="[entityName]"/> : ${sampleTime.sampleTime}</h1>
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

        <th><g:message code="broker.stage.label"
                       default="Environment"/></th>

        <th><g:message code="broker.site.label" default="Site"/></th>

        <th><g:message code="broker.info.heapMemory" default="Site"/></th>

        <th><g:message code="broker.info.nonHeapMemory" default="Site"/></th>

        <th><g:message code="broker.info.load" default="Site"/></th>

        <th><g:message code="broker.info.cpu" default="Site"/></th>

      </tr>
      </thead>
      <tbody>
      <g:each in="${brokerInstanceList}" status="i" var="brokerInstance">
        <g:set var="info" value="${brokerInfo[brokerInstance]}"/>
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <td><g:link action="show"
                      id="${brokerInstance.id}">${fieldValue(bean: brokerInstance, field: "id")}</g:link></td>

          <td>${fieldValue(bean: brokerInstance, field: "node")}</td>

          <td>${fieldValue(bean: brokerInstance, field: "stage")}</td>

          <td>${fieldValue(bean: brokerInstance, field: "site")}</td>

          <td>
            <g:if test="${info!=null}">
              <sparklines:line id="heap-trend-${info.id}"
                               values="${trendInfoHeap[brokerInstance]}"/>
              <sparklines:pie id="heap-${info.id}"
                              values="${[info.usedHeap, info.maxHeap - info.usedHeap]}"/>
                ${Math.round(info.usedHeap/1000000)}Mb/${Math.round(info.maxHeap/1000000)}Mb
            </g:if>
            <g:else>no data available</g:else>
          </td>

          <td>
            <g:if test="${info!=null}">
              <sparklines:line id="non-heap-trend-${info.id}"
                               values="${trendInfoNonHeap[brokerInstance]}"/>
              <sparklines:pie id="non-heap-${info.id}"
                              values="${[info.usedNonHeap, info.maxNonHeap - info.usedNonHeap]}"/>
              ${Math.round(info.usedNonHeap/1000000)}Mb/${Math.round(info.maxNonHeap/1000000)}Mb
            </g:if>
            <g:else>no data available</g:else>
          </td>

          <td>
            <g:if test="${info!=null}">
              <sparklines:line id="load-trend-${info.id}"
                               values="${trendInfoLoad[brokerInstance]}"/>
               ${info.loadAverage}
            </g:if>
            <g:else>no data available</g:else>
          </td>

          <td>
            <g:if test="${info!=null}">
              <sparklines:line id="cpu-trend-${info.id}"
                               values="${trendInfoCpu[brokerInstance]}"/>
               ${cpuAverage[brokerInstance]}
            </g:if>
            <g:else>no data available</g:else>
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
