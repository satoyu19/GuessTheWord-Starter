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
  ・ViewModelはそれに関連するフラグメントが取り外されたとき、またはアクティビティが終了したときに破棄されます。
    ViewModelが破棄される直前にonCleared()コールバックがリソースをクリーンアップするために呼び出されます。
  ・ViewModelはUI controllerに紐づけられる必要があります。それら二つを紐づけるためには、UI controllerの中でViewModelの参照を作成します。
    フラグメントの中で、それに対するViewModel型のクラス変数をフィールドとして定義する。
  ・画面回転のようなコンフィグレーション変化の間、フラグメントのようなUI controllerは再生成されます。しかしながら、ViewModelインスタンスは引き継がれます。
    もしViewModelインスタンスをViewModelクラスを使って作っていると、フラグメントが再生成される度に、新しいオブジェクトが作られることになります。
    ですのでViewModelインスタンスはViewModelProviderを使って作成しましょう。
  ![image](https://user-images.githubusercontent.com/96398365/172641634-bbb661b8-ae95-4bef-aa27-6485721cc4c4.png)

***ViewModelProviderの機能***<br>
  ・ViewModelProviderはViewModelが既に存在している場合、そのViewModelを返します。存在しない場合は新しく作成します。
  ・ViewModelProviderは与えられたスコープ（アクティビティやフラグメント）に紐づいたViewModelインスタンスを作成します。
  ・生成されたViewModelはスコープが有効である限り、保持されます。例として、スコープがフラグメントである場合、ViewModelはフラグメントが取り外されるまで保持されます。
  
  ・ViewModelはコンフィグレーション変化の前後でも生き残るので、コンフィグレーション変化に対応させたいデータを置く場所として適しています。
  
  ・ViewModelにはフラグメント、アクティビティ、またはビューなどの参照を含ませるべきではありません。
  なぜならそれらはコンフィグレーション変化の後に生き残らないからです。(Fragment等は、コンフィグレーション変化によって再生成されるため保持しても意味がない)
  
  ViewModelFactory
------------
 ・ファクトリーメソッドパターンはオブジェクトを作るためにファクトリーメソッドを使う生成デザインパターンの一つです。ファクトリーメソッドは同じクラスのインスタンスを返すメソッドです。
    ViewModelとは別にViewModelFactoryクラスを用意する。

![image](https://user-images.githubusercontent.com/96398365/172636919-2749f43c-fcf2-4c93-a029-ba62a2b2ef05.png)

