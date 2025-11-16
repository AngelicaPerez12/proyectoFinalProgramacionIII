Instrucciones para fuentes personalizadas

Coloca las fuentes que quieres usar en la carpeta `src/main/resources/com/example/proyectoprogra/Styles/fonts/`.

Archivos esperados por la aplicación (nombres exactos):
- PlayfairDisplay-Regular.ttf
- Roboto-Regular.ttf

Descarga sugerida (fuentes libres desde Google Fonts):
- Playfair Display: https://fonts.google.com/specimen/Playfair+Display
- Roboto: https://fonts.google.com/specimen/Roboto

Pasos rápidos:
1. Abre los enlaces anteriores y descarga las familias. 
2. Extrae los archivos .ttf y renómbralos (si es necesario) a los nombres listados arriba.
3. Copia los archivos a `src/main/resources/com/example/proyectoprogra/Styles/fonts/`.
4. Reinicia la aplicación.

Notas:
- `HelloApplication` intenta cargar los archivos en runtime. Si no están presentes, la aplicación seguirá usando fuentes del sistema como fallback.
- Asegúrate de que los archivos no estén bloqueados por tu sistema operativo (OneDrive puede marcar archivos como online-only). Si la fuente no carga, copia localmente en el proyecto y vuelve a ejecutar.
- Licencia: Google Fonts distribuye estas familias bajo licencias libres (SIL Open Font License o similares). Revisa las licencias si vas a redistribuir la aplicación.

Si quieres, puedo descargar e incluir las fuentes en el repositorio por ti (necesitaría permiso para añadir archivos binarios y confirmación de que quieres las fuentes embebidas).
