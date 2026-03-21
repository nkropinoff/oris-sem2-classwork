<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <style>
        body { font-family: sans-serif; margin: 40px; color: #111; }
        a { color: #111; }
        a:hover { color: #c00; }
        h2 { margin-bottom: 20px; }
        .error { color: #c00; margin-bottom: 12px; }
        .form-group { margin-bottom: 12px; }
        label { display: block; margin-bottom: 4px; }
        input { padding: 6px 8px; border: 1px solid #ccc; font-size: 14px; width: 260px; }
        button, .btn { padding: 5px 12px; border: 1px solid #111; background: #f0f0f0; cursor: pointer; font-size: 14px; font-weight: bold; text-decoration: none; color: #111; }
        button:hover, .btn:hover { background: #c00; color: #fff; border-color: #c00; }
        .links { margin-top: 14px; font-size: 14px; display: flex; gap: 20px; }
    </style>
</head>
<body>
    <h2>Регистрация</h2>

    <#if error??>
        <div class="error">${error}</div>
    </#if>

    <form action="/register" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="form-group">
            <label>Логин</label>
            <input type="text" name="username" required/>
        </div>
        <div class="form-group">
            <label>Пароль</label>
            <input type="password" name="password" required/>
        </div>
        <button type="submit">Зарегистрироваться</button>
    </form>

    <div class="links">
        <a href="/index">← На главную</a>
        <a href="/login">Уже есть аккаунт? Войти</a>
    </div>
</body>
</html>
