<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <@l.logout/>
List of users
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Role</th>
        <th>Commands</th>
    </tr>
    </thead>
    <tbody>
<#list users as user>
<tr>
    <td>${user.username}</td>
    <td><#list user.roles as role>${role}<#sep>, </#list></td>
    <td><a href="/user/${user.id}">edit</a></td>

</tr>
<#else> No users
</#list>
    </tbody>
</table>
</@c.page>