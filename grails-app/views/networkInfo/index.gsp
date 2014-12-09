<%@ page import="esbmon.BridgeInfo; org.apache.activemq.command.BrokerInfo; esbmon.NetworkInfo; esbmon.Broker" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <meta http-equiv="refresh" content="60">
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
  <h1><g:message code="default.list.label" args="[entityName]"/> : ${sampleTime?.sampleTime}</h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="list">
    <table>
      <thead>
      <tr>
        <th>...</th>
        <g:each in="${brokerNames}" status="i" var="brokerName">
            <th>${brokerName}</th>
        </g:each>
      </tr>
      </thead>
      <tbody>
      <g:each in="${connectorNames}" status="i" var="connector">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <th>${connector}</th>

            <g:each in="${brokerNames}" status="j" var="broker">
                <%
                    def key = "${broker}:${connector}"
                    def networkInfo = bridges[key]
                    def remotes = networkInfo?.bridges
                %>
                <g:if test="${networkInfo != null}">
                    <g:if test="${remotes != 0}">
                        <td>
                            <g:each in="${remotes}" status="k" var="remote">
                                <table>
                                    <tr>
                                        <td><sparklines:line id="n${i}c${j}b${k}e" values="${trends[key][0]}"/></td>
                                        <td>${remote.dequeueCounter}</td>
                                    </tr>
                                    <tr>
                                      <td><sparklines:line id="n${i}c${j}b${k}d" values="${trends[key][1]}"/></td>
                                      <td>${remote.enqueueCounter}</td>
                                    </tr>
                                </table>
                            </g:each>
                        </td>
                    </g:if>
                    <g:else>
                        <td>no bridge</td>
                    </g:else>
                </g:if>
                <g:else>
                    <td>&nbsp;</td>
                </g:else>
            </g:each>
        </tr>
      </g:each>
      </tbody>
    </table>
  </div>

</div>
</body>
</html>
