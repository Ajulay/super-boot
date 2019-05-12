<#macro login path>
    <form method="post" action="${path}">
        <div><label>Username: <input type="text" name="username" placeholder="username"></label></div>
        <div><label>Password: <input type="password" name="password" placeholder="password"></label></div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit"> login </button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Log out</button>
    </form>
</#macro>