# HandleOffice
This project is used to convert office document to pdf by using docx4j

This project is built by docx4j by using Android Studio
My step of it is:
1. Add the dependencies of docx4j in the lib and the source code in the main dir, then clear all the compile errors
2. Modified some classes which use the class of sun.awt.AppContent by using the android.graphics.Bitmap
  Reason:
  If you don't replace it, the project is not able to run on the android by Error of ClassNotFoundError if the document contains a image
3. For the version 1.0:
  Only gurantee this project can be run on android device successfully, the pdf conversion feature is not added into this component
  I will use the itextG to realize this feature.
  
ATTENTION:
  Version 1.0:
  1. DOCX -> HTML
  2. AutoShape is not supported
  3. PPTX/XLSX are not supported
  4. PDF is not supported
