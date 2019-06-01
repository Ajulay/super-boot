<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <@l.logout/>
<span><a href="/user">user list</a></span>
<div>
    <form method="post" action="/main" enctype="multipart/form-data">
        <input type="text" name="text" placeholder="Enter message">
        <input type="text" name="tag" placeholder="Tag">
        <input type="file" name="file">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit"> Add</button>
    </form>
</div>
<form method="get" action="/main">
    <input type="text" name="tag" value="${tag?if_exists}">
    <button type="submit">Find</button>
</form>
<div> Message list </div>
    <#list messages as message>
    <div>
        <b>${message.id}</b>
        <span>${message.text}</span>
        <i>${message.tag}</i>
        <span>${message.authorName}</span>
        <div>
            <#if message.filename??>
                <img src="/img/${message.filename}">
            </#if>
        </div>
    </div>
    <#else> No messages
    </#list>
</@c.page>