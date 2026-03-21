<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Публичные заметки</title>
    <style>
        body { font-family: sans-serif; margin: 40px; color: #111; }
        a { color: #111; text-decoration: none; }
        a:hover { color: #c00; }
        nav { margin-bottom: 32px; display: flex; gap: 20px; border-bottom: 1px solid #ccc; padding-bottom: 12px; }
        nav .active { font-weight: bold; }
        h1 { margin-bottom: 20px; font-size: 20px; }
        .note-card { border: 1px solid #ccc; padding: 14px; margin-bottom: 10px; }
        .note-card h3 { margin: 0 0 6px; font-size: 16px; }
        .note-meta { font-size: 12px; color: #666; margin-bottom: 8px; }
        .note-content { font-size: 14px; }
        .empty { color: #666; }
        .btn { padding: 5px 12px; border: 1px solid #111; background: #f0f0f0; font-weight: bold; text-decoration: none; color: #111; }
        .btn:hover { background: #c00; color: #fff; border-color: #c00; }
    </style>
</head>
<body>
    <nav>
        <a href="/index">Главная</a>
        <a href="/notes/public" class="active">Публичные заметки</a>
        <span style="flex:1"></span>
        <a href="/login">Войти</a>
        <a href="/register" class="btn">Регистрация</a>
    </nav>

    <h1>Публичные заметки</h1>

    <#if publicNotes?has_content>
        <#list publicNotes as note>
            <div class="note-card">
                <h3>${note.title?html}</h3>
                <div class="note-meta">
                    ${note.author.username?html} · ${note.createdAt}
                </div>
                <div class="note-content">${note.content?html}</div>
            </div>
        </#list>
    <#else>
        <p class="empty">Публичных заметок пока нет.</p>
    </#if>
</body>
</html>
