<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Заметки</title>
    <style>
        body { font-family: sans-serif; margin: 40px; color: #111; }
        a { color: #111; text-decoration: none; }
        a:hover { color: #c00; }
        nav { margin-bottom: 32px; display: flex; gap: 20px; border-bottom: 1px solid #ccc; padding-bottom: 12px; align-items: center; }
        .btn { padding: 5px 12px; border: 1px solid #111; background: #f0f0f0; font-weight: bold; }
        .btn:hover { background: #c00; color: #fff; border-color: #c00; }
    </style>
</head>
<body>
    <nav>
        <a href="/index">Главная</a>
        <a href="/notes/public">Публичные заметки</a>
        <a href="/notes">Мои заметки</a>
        <span style="flex:1"></span>
        <a href="/login">Войти</a>
        <a href="/register" class="btn">Регистрация</a>
    </nav>

    <p>Добро пожаловать. <a href="/register">Зарегистрируйтесь</a> или <a href="/login">войдите</a>.</p>
</body>
</html>
