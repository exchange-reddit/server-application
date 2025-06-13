-- Populate posts table with sample data

INSERT INTO post (
    title,
    content,
    author_id,
    community_id,
    is_deleted,
    status,
    created_at,
    updated_at,
    attachment_path
) VALUES
(
    'First Post',
    'This is the content of the first post.',
    1,
    1,
    FALSE,
    'PUBLISHED',
    NOW(),
    NOW(),
    '/uploads/first-post.pdf'
),
(
    'Second Post',
    'Another interesting post about the community.',
    2,
    1,
    FALSE,
    'DRAFT',
    NOW(),
    NOW(),
    '/uploads/second-image.png'
),
(
    'Archived Post',
    'This post has been archived.',
    3,
    2,
    FALSE,
    'ARCHIVED',
    NOW(),
    NOW(),
    NULL
),
(
    'Deleted Post',
    'This post is marked as deleted.',
    4,
    2,
    TRUE,
    'PUBLISHED',
    NOW(),
    NOW(),
    '/uploads/deleted-file.txt'
),
(
    'Yet Another Post',
    'Content for yet another post.',
    1,
    3,
    FALSE,
    'PUBLISHED',
    NOW(),
    NOW(),
    '/uploads/yet-another-post.docx'
);
