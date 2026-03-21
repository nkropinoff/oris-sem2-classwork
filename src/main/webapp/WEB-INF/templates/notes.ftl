<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои заметки</title>
    <style>
        body { font-family: sans-serif; margin: 40px; color: #111; }
        a { color: #111; text-decoration: none; }
        a:hover { color: #c00; }
        nav { margin-bottom: 32px; display: flex; gap: 20px; border-bottom: 1px solid #ccc; padding-bottom: 12px; align-items: center; }
        nav .active { font-weight: bold; }
        h1 { margin-bottom: 20px; font-size: 20px; }
        .note-card { border: 1px solid #ccc; padding: 14px; margin-bottom: 10px; }
        .note-card h3 { margin: 0 0 6px; font-size: 16px; }
        .note-meta { font-size: 12px; color: #666; margin-bottom: 8px; }
        .note-content { font-size: 14px; margin-bottom: 10px; }
        .note-actions { display: flex; gap: 8px; }
        .badge { font-size: 11px; border: 1px solid #ccc; padding: 1px 6px; }
        .empty { color: #666; }
        button, .btn { padding: 5px 12px; border: 1px solid #111; background: #f0f0f0; cursor: pointer; font-size: 13px; font-weight: bold; text-decoration: none; color: #111; }
        button:hover, .btn:hover { background: #c00; color: #fff; border-color: #c00; }
    </style>
</head>
<body>
    <nav>
        <a href="/index">Главная</a>
        <a href="/notes" class="active">Мои заметки</a>
        <a href="/notes/public">Публичные заметки</a>
        <a href="/notes/create" class="btn" style="margin-left:auto">+ Создать</a>
        <form method="post" action="/logout" style="margin:0">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Выйти</button>
        </form>
    </nav>

    <h1>Мои заметки</h1>

    <#if userNotes?has_content>
        <#list userNotes as note>
            <div class="note-card">
                <h3>${note.title?html}</h3>
                <div class="note-meta">
                    <span class="badge"><#if note.public>публичная<#else>приватная</#if></span>
                    &nbsp;${note.createdAt}
                </div>
                <div class="note-content">${note.content?html}</div>
                <div class="note-actions">
                    <a href="/notes/${note.id}/edit" class="btn">Редактировать</a>
                    <form method="post" action="/notes/${note.id}/delete"
                          onsubmit="return confirm('Удалить?')" style="margin:0">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <button type="submit">Удалить</button>
                    </form>
                </div>
            </div>
        </#list>
    <#else>
        <p class="empty">Нет заметок. <a href="/notes/create">Создать?</a></p>
    </#if>
</body>
</html>
