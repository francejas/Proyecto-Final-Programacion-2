Clonar el repositorio
git clone LINK
cd proyecto-final

Cambiar a la rama de desarrollo
git checkout develop
git pull origin develop
Importante: nunca trabajar directamente sobre main.


Flujo de trabajo (Git Flow)

Usamos tres tipos de ramas principales:

Rama	Propósito
main	Versión estable y final del sistema
develop	Rama de integración (donde se unen las features)
feature/nombre-de-la-tarea	Rama temporal de cada integrante para desarrollar una funcionalidad


Crear una rama para tu tarea
git checkout develop
git pull origin develop
git checkout -b feature/nombre-de-la-tarea



Subir cambios
git add .
git commit -m "Agrego [nombre de la funcionalidad]"
git push origin feature/nombre-de-la-tarea


🔁 Crear un Pull Request (PR)

Desde GitHub, crear un Pull Request desde tu rama feature/... hacia develop.

Un compañero o el responsable revisará el código antes de hacer el merge.



📋 Project Board (gestión de tareas)

Todas las tareas se administran en el tablero de GitHub Projects:
👉 🔗 Enlace al tablero de tareas

Columnas del tablero:

🟡 Pendiente / To do → tareas sin comenzar

🔵 En progreso / In progress → tareas en desarrollo

🟢 Finalizado / Done → tareas completadas o mergeadas

⚙️ El tablero se actualiza automáticamente cuando se crean o cierran issues y PRs.

⚠️ Buenas prácticas

✅ Siempre crear ramas desde develop
✅ Mensajes de commit claros y en presente (“Agrego”, “Corrijo”, etc.)
✅ No subir archivos innecesarios (__pycache__, venv, etc.)
✅ Revisar el .gitignore antes de hacer add .
✅ Hacer pull antes de empezar a trabajar cada día
