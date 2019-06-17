<#macro login path isRegisterForm>
    <form method="post" action="${path}">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Username: </label>
            <div class="col-sm-5">
                <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}" placeholder="user name">
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password: </label>
            <div class="col-sm-5">
                <input type="password" name="password" placeholder="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}">
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
    <#if isRegisterForm>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Confirm password: </label>
            <div class="col-sm-5">
                <input type="password" name="password2" placeholder="confirm password"
                       class="form-control ${(password2Error??)?string('is-invalid', '')}">
                <#if password2Error??>
                    <div class="invalid-feedback">
                        ${password2Error}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email: </label>
            <div class="col-sm-5">
                <input type="email" name="email" placeholder="email@email.com" value="<#if user??>${user.email}</#if>"
                       class="form-control ${(emailError??)?string('is-invalid', '')}">
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="col-sm-5">
            <div class="g-recaptcha" data-sitekey="6Lcp46gUAAAAAPz_2lryfE9LgoIz3zbtw4StJGbb"></div>
            <#if recaptchaError??>
                <div class="alert alert-danger" role="alert">${recaptchaError}</div>
            </#if>
        </div>
    </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <#if !isRegisterForm>
            <a href="/registration">add new user</a>
        </#if>
        <button class="btn btn-primary" type="submit"><#if isRegisterForm>Create<#else>Sign In</#if></button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button class="btn btn-primary" type="submit">Log Out</button>
    </form>
</#macro>