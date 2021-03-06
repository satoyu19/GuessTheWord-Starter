使用技術
==================================

・viewModel


アプリアーキテクチャー
------------

  ・アプリアーキテクチャはアプリのクラスやクラス間の関係をデザインするための方法の一つで、これによりコードをきれいにし、
  特定の状況下で効率よく処理させたり、簡単に動作させることができます。Androidアプリアーキテクチャガイドラインに従って、
  Androidアーキテクチャコンポーネントを使用していきます。AndroidアプリアーキテクチャはMVVM(model-view-viewmodel)パターンによく似ています。

  UI controller
------------
 ・UI controllerはActivityやFragmentのようなUIベースのクラスです。UI controllerにはビューやユーザーインプットを捕捉するようなUIや対話OSを操作するロジックのみが含まれるべきです。
 テキストを表示するためのロジックのような意思決定論理はUI controllerの中には入れてはいけません。

  ViewModel
------------
 ・ViewModelはViewModelに関連するフラグメントやアクティビティに表示されているデータを保持します。
ViewModelはUI controllerによって表示されるデータを準備するための単純な計算やデータの変換などを行います。このアーキテクチャでは、ViewModelが意思決定を行います。

***利用方法***<br>
  ・dependenciesブロックの中に、ViewModel用のGradle依存関係を追加、バージョン確認要
  ・ViewModelはそれに関連するフラグメントが取り外されたとき、またはアクティビティが終了したときに破棄されます。<br>
    ViewModelが破棄される直前にonCleared()コールバックがリソースをクリーンアップするために呼び出されます。<br>
  ・ViewModelはUI controllerに紐づけられる必要があります。それら二つを紐づけるためには、UI controllerの中でViewModelの参照を作成します。<br>
    フラグメントの中で、それに対するViewModel型のクラス変数をフィールドとして定義する。<br>
  ・画面回転のようなコンフィグレーション変化の間、フラグメントのようなUI controllerは再生成されます。しかしながら、ViewModelインスタンスは引き継がれます。<br>
    もしViewModelインスタンスをViewModelクラスを使って作っていると、フラグメントが再生成される度に、新しいオブジェクトが作られることになります。<br>
    ですのでViewModelインスタンスはViewModelProviderを使って作成しましょう。
  ![image](https://user-images.githubusercontent.com/96398365/172641634-bbb661b8-ae95-4bef-aa27-6485721cc4c4.png)

***ViewModelProviderの機能***<br>
  ・ViewModelProviderはViewModelが既に存在している場合、そのViewModelを返します。存在しない場合は新しく作成します。<br>
  ・ViewModelProviderは与えられたスコープ（アクティビティやフラグメント）に紐づいたViewModelインスタンスを作成します。<br>
  ・生成されたViewModelはスコープが有効である限り、保持されます。例として、スコープがフラグメントである場合、ViewModelはフラグメントが取り外されるまで保持されます。<br>
  
  ・ViewModelはコンフィグレーション変化の前後でも生き残るので、コンフィグレーション変化に対応させたいデータを置く場所として適しています。<br>
  
  ・ViewModelにはフラグメント、アクティビティ、またはビューなどの参照を含ませるべきではありません。
  なぜならそれらはコンフィグレーション変化の後に生き残らないからです。(Fragment等は、コンフィグレーション変化によって再生成されるため保持しても意味がない)
  
  ViewModelFactory
------------
 ・ファクトリーメソッドパターンはオブジェクトを作るためにファクトリーメソッドを使う生成デザインパターンの一つです。ファクトリーメソッドは同じクラスのインスタンスを返すメソッドです。
    ViewModelとは別にViewModelFactoryクラスを用意する。

![image](https://user-images.githubusercontent.com/96398365/172636919-2749f43c-fcf2-4c93-a029-ba62a2b2ef05.png)

  LiveDate
------------
・LiveDataはライフサイクル対応の監視可能なデータホルダークラスです。<br>

・LiveDataは監視可能です。これはLiveDataオブジェクトによって保持されているデータに変更があったときにオブザーバーに通知されることを意味しています。<br>
・LiveDataはデータを保持します。LiveDataはどんなデータにも用いることができるラッパーです。<br>
・LiveDataはライフサイクル対応です。オブザーバーをLiveDataに取り付けると、オブザーバーはLifecycleOwner(通常はアクティブやフラグメント）と紐づけられます。<br>
・LiveDataはSTARTEDやRESUMEDのようなアクティブなライフサイクル状態の中にあるオブザーバーのみを更新します。<br>
詳しくはhttps://developer.android.com/topic/libraries/architecture/livedata.html#work_livedataを参照。<br>

・ObserverオブジェクトをそれらのLiveDataオブジェクトに取り付けます。フラグメントビュー(viewLifecycleOwner)をLifecycleOwnerとして使います。

***viewLifecycleOwnerを使う理由***<br>
フラグメントのビューはユーザーがそのフラグメントから離れた際に破棄されます。たとえそのフラグメント自身が破棄されない場合であってもです。これによって本来二つのライフサイクルが生成されます。フラグメントのライフサイクルとフラグメントのビューのライフサイクルです。フラグメントのビューのライフサイクルではなく、フラグメントのライフサイクルを使うと、フラグメントのビューを更新した際に細かいバグが発生することがあります。従って、フラグメントのビューに影響を与えるオブザーバーをセットアップする際は、以下のようにします。<br>

1. onCreateView()の中でオブザーバーをセットアップする。<br>
2. オブザーバーにviewLifecycleOwnerを渡す。<br>

アプリのデータをカプセル化するためには、MutableLiveDataとLiveDataオブジェクト両方を使います。<br>

***注意***<br>
・通常、LiveDateはデータが変更した場合にのみオブザーバーに変更を知らせるが、オブザーバーが非アクティブ状態からアクティブ状態に変化した際にも更新を受け取ってしまう。フラグメントが画面回転の後で再生成される際、非アクティブからアクティブ状態となり、。フラグメントのオブザーバーは存在するViewModelに再接続され、現在のデータを受け取る。<br>

***LiveDateの変換***<br>
 ・Transformations.map()メソッドはソースとなるLiveDataのデータを操作する手段で、操作結果のLiveDataオブジェクトを返します。これらの変換はオブザーバーが返されるLiveDataオブジェクトを監視していない限り、計算処理されません。<br>
 
 tip: Transformation.map()に渡されるラムダ式はメインスレッドで実行されるので、時間のかかるタスクは含まれるべきではありません。

  オブザーバーパターン
------------
・オブザーバーパターンはソフトウェアデザインパターンの一つです。これは監視可能なオブジェクト（監視されるオブジェクト）とオブザーバー（監視するオブジェクト）の二つのオブジェクト間での情報伝達についてのパターンです。監視される側はオブザーバーに自らの状態が変化したことを通知するオブジェクトです。
![image](https://user-images.githubusercontent.com/96398365/172860360-4efbe142-bf4b-4609-9085-ff99aefa76a9.png)

ViewModelデータバインディング  
------------
・仲介役としてのUI controller無しに、レイアウトのビューがViewModelオブジェクト内のデータと直接やり取りすれば、より簡素化することができます。ViewModelオブジェクトをデータバインディングに渡すことで、ビューとViewModelオブジェクト間のやり取りを自動化することができます。
![image](https://user-images.githubusercontent.com/96398365/172873204-0c7a4675-c49c-47c4-8860-60b5f52fdb30.png)
レイアウトではデータバインディングと共にstringフォーマットを追加することができます。

***注意***<br>
・アプリがデータバインディングを使用する際、コンパイル処理によってデータバインディングのために使われる中間クラスが生成されます。それによりアプリをコンパイルしようとするまでAndroid Studioが検知できないエラーがアプリに含まれている場合があり、コードを書いている際には警告や赤文字が表示されません。しかしコンパイル時に生成された中間クラスから発生する一見不可解なエラーに遭遇する場合あります。

companion object
------------
・クラス内に作成されるSingleton,1クラスに1つのみ宣言可能,Kotlinにはstatic修飾子がないので、companion objectはstaticなフィールドやメソッドが必要なときの代替手段として利用されることがほとんどだと思います。
