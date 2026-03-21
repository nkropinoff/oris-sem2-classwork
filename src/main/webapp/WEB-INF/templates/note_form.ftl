<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if (note.title)?has_content>Редактировать<#else>Создать заметку</#if></title>
    <style>
        body { font-family: sans-serif; margin: 40px; color: #111; }
        a { color: #111; text-decoration: none; }
        a:hover { color: #c00; }
        nav { margin-bottom: 32px; display: flex; gap: 20px; border-bottom: 1px solid #ccc; padding-bottom: 12px; align-items: center; }
        h1 { margin-bottom: 20px; font-size: 20px; }
        .form-group { margin-bottom: 14px; }
        label { display: block; margin-bottom: 4px; font-size: 14px; }
        input[type="text"], textarea { padding: 6px 8px; border: 1px solid #ccc; font-size: 14px; width: 480px; font-family: sans-serif; }
        textarea { min-height: 200px; resize: vertical; }
        .checkbox-group { display: flex; align-items: center; gap: 8px; }
        .actions { display: flex; gap: 10px; margin-top: 18px; }
        button, .btn-cancel { padding: 5px 12px; border: 1px solid #111; background: #f0f0f0; cursor: pointer; font-size: 14px; font-weight: bold; text-decoration: none; color: #111; }
        button:hover, .btn-cancel:hover { background: #c00; color: #fff; border-color: #c00; }
    </style>
</head>
<body>
    <nav>
        <a href="/index">Главная</a>
        <a href="/notes">Мои заметки</a>
        <a href="/notes/public">Публичные заметки</a>
        <form method="post" action="/logout" style="margin:0;margin-left:auto">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Выйти</button>
        </form>
    </nav>

    <h1><#if (note.title)?has_content>Редактировать заметку<#else>Создать заметку</#if></h1>

    <form method="post" action="${formAction}">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="form-group">
            <label>Заголовок</label>
            <input type="text" name="title" value="${(note.title)!''}" required maxlength="255">
        </div>
        <div class="form-group">
            <label>Содержимое</label>
            <textarea name="content" required>${(note.content)!''}</textarea>
        </div>
        <div class="form-group">
            <div class="checkbox-group">
                <input type="checkbox" id="public" name="public" <#if (note.public)!false>checked</#if>>
                <label for="public" style="margin:0">Публичная</label>
            </div>
        </div>
        <div class="actions">
            <button type="submit">Сохранить</button>
            <a href="/notes" class="btn-cancel">Отмена</a>
        </div>
    </form>
</body>
</html>
