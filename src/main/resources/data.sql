-- Resetea la tabla 
TRUNCATE TABLE usuarios;

-- Inserta usuarios de prueba 
INSERT INTO usuarios (username, email, contrasena, fecha_registro) VALUES
    ('gamerPro99', 'gamer99@volvo.cl', 'clave123', '2026-06-20'),
    ('sol_fire', 'sol.fire@gmail.com', 'solcito2026', '2026-06-21'),
    ('claudio_dev', 'claudio@outlook.com', 'springsecure', '2026-06-22'),
    ('admin_volvo', 'admin@volvo.cl', 'admin2026', '2026-06-23'),
    ('pixel_art', 'pixel@art.com', 'dibujo456', '2026-06-23');