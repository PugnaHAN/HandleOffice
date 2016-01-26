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

-----------------------------------------Update on Jan 26th 2016---------------------------------------
使用SlidingTab来替代TabHost
综诉：
在这个版本下，主要更新的是UI，原本的代码是采用TabHost。但是，由于管理低下等原因，TabHost已经不被谷歌建议使用。这个版本采用的是fragment和SlideTab来管理。
1. SlideTab来源于谷歌的一个demo，将其中两个文件（本工程view目录下的文件）
2. 建立一个layout，工程中的fragment-layout，添加SlidingTabLayout和ViewPager为其中的元素。
3. 创建一个BaseFragment来让不同的Fragment继承，本工程中包含有一个WebView的WebFragment， 包含一个TextView的CodeFragment以及包含一个ImageView的PdfFragemtn，作用：
  a. WebFragment - 显示docx转化过的html文件
  b. CodeFragment - 显示html源代码
  c. PdfFragment - 这是本工程中最重要的一个，但是目前没有添加功能，用来显示PDF
4. 新建一个TabFragmentAdpter来管理需要载入的不同fragments数据
5. 新建TabFragment来包含tab和viewpager
6. 新建一个Thread用来处理docx4的转化工作 -》 后期docx转pdf需要主要修改的地方
7. 在TabFragment中添加更新UI的代码，以便于在docx转化成功后正确更新UI

注意事项：
1. ProgressBar之前还需要添加一个FrameLayout，并以这个FrameLayout作为content_fragment(TabFragment的布局文件)，这样才能保证ProgressBar不会被覆盖（如果用根FrameLayout作为TabFragment的布局文件的话，ProgressBar会看不见）
2. 如果在WebFragment等文件中，如果不在OnCreateView中对其包含的View（如WebView）进行载入数据的话，在滑动Tab到其隔壁的隔壁Tab时（比如PDF tab 是 WebView Tab的隔壁的隔壁），其数据将被清空（待验证：是否重新调用了OnCreateView，可以添加Log确认 -其原理应该和FragmentPagerAdapter有关，应该是为了节省资源）

