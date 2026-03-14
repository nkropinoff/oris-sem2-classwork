<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .card {
            background: #fff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            width: 360px;
        }

        h2 {
            margin-bottom: 24px;
            font-size: 24px;
            color: #333;
            text-align: center;
        }

        .error {
            background: #ffe0e0;
            color: #cc0000;
            padding: 10px 14px;
            border-radius: 8px;
            margin-bottom: 16px;
            font-size: 14px;
        }

        .form-group {
            margin-bottom: 16px;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-size: 14px;
            color: #555;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px 14px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 15px;
            transition: border-color 0.2s;
            outline: none;
        }

        input[type="text"]:focus,
        input[type="password"]:focus {
            border-color: #4f8ef7;
        }

        button {
            width: 100%;
            padding: 12px;
            background: #4f8ef7;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            margin-top: 8px;
            transition: background 0.2s;
        }

        button:hover {
            background: #3a75d4;
        }

        .login-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: #666;
            text-decoration: none;
        }

        .login-link:hover {
            color: #4f8ef7;
        }
    </style>
</head>
<body>

<div class="card">
    <h2>Регистрация</h2>

    <#if error??>
        <div class="error">${error}</div>
    </#if>

    <form action="/register" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <div class="form-group">
            <label>Логин</label>
            <input type="text" name="username" placeholder="Введите логин" required/>
        </div>

        <div class="form-group">
            <label>Пароль</label>
            <input type="password" name="password" placeholder="Введите пароль" required/>
        </div>

        <button type="submit">Зарегистрироваться</button>
    </form>

    <a class="login-link" href="/login">Уже есть аккаунт? Войти</a>
</div>

</body>
</html>