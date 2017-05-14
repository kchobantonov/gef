# [GEF 5.0.0 (Oxygen)](https://projects.eclipse.org/projects/tools.gef/releases/5.0.0-oxygen)

Annual release providing major revisions (5.0.0) of all production components, which have been adopted to the original project namespace (['GEF4 + 1 = GEF 5'](http://nyssen.blogspot.de/2017/02/gef4-1-gef-5.html)) and are now consistently referred to as GEF components.

# [GEF 4.1.0 (Neon.1)](https://projects.eclipse.org/projects/tools.gef/releases/4.1.0-neon.1)

Update release providing minor (1.1.0) respectively micro (1.0.1) revisions of all GEF4 components (Common, Geometry, FX, MVC, Graph, Layout, Zest, DOT, and Cloudio). All API extensions are motivated by bugfixing, the API is fully backwards compatible to that of the previous GEF 4.0.0 (Neon) release.

# [GEF 4.0.0 (Neon)](https://projects.eclipse.org/projects/tools.gef/releases/4.0.0-neon)

Annual release providing first graduation revisions (1.0.0) of the previously only preliminary published GEF4 components (Common, Geometry, FX, MVC, Graph, Layout, Zest, DOT, and Cloudio).

*As declared in the [4.0.0 (Neon) project plan](https://projects.eclipse.org/projects/tools.gef/releases/4.0.0-neon/plan), the decision about contributing GEF4 in version 1.0.0 (instead of 0.3.0) and an overall 4.0.0 release (instead of 3.11.0) was postponed up to M5. That is, Bugzilla entries commented before Neon M6 will refer to a 3.11.0 release and milestone contributions (including M5) include GEF4 components in version 0.3.0.*

Please note that several incompatible changes to the (up to 0.2.0 provisional) API of GEF4 were made. The list of added and removed classes is documented in the [GEF4 0.2.0-1.0.0 API Diff](https://www.eclipse.org/gef/project-info/GEF4-0.2.0-1.0.0-API-Diff.html). The most notable API changes are outlined below.

### GEF4 Common (1.0.0)

* [#484774](https://bugs.eclipse.org/bugs/show_bug.cgi?id=484774) As outlined in detail in ['GEF4 Collections and Properties - Guava goes FX'](http://nyssen.blogspot.de/2016/04/gef4-common-collections-and-properties.html) the property notification support provided by Common has been replaced with JavaFX observable collections and properties. In this turn, the Common component has been augmented to provide observable collections and related collection properties for Guava's SetMultimap and Multiset, as well as replacements for the ObservableSet, ObservableList, and ObservableMap collections and related collection properties provided by JavaFX.

* [#482972](https://bugs.eclipse.org/bugs/show_bug.cgi?id=482972) The adapter map injection support, described in detail in ['Adaptable - GEF4's interpretation of a classic'](http://nyssen.blogspot.com/2014/11/iadaptable-gef4s-interpretation-of.html), has been updated to ensure that adapter can always been retrieved via their actual runtime type, as well as been augmented with support for role-based adapter injection.

* [#481677](https://bugs.eclipse.org/bugs/show_bug.cgi?id=481677) Adapter map bindings can now be restricted to an adaptable of a certain role. Thereby, different bindings can e.g. be provided for a 'content' <code>FXViewer</code> and a 'palette' <code>FXViewer</code>.

~~~java
@Override
protected void configure() {
  // bind adapters for FXRootPart of role 'FXDomain.CONTENT_VIEWER_ROLE'
  bindContentViewerRootPartAdapters(AdapterMaps.getAdapterMapBinder(binder(), FXRootPart.class, FXDomain.CONTENT_VIEWER_ROLE));
  ... 
}
~~~

### GEF4 FX (1.0.0)

* [#471154](https://bugs.eclipse.org/bugs/show_bug.cgi?id=471154) Renamed FX.UI module into FX.SWT module and added <code>FXColorPicker</code>, <code>FXSimpleLinearGradientPicker</code>, and <code>FXAdvancedLinearGradientPicker</code>, which were provided by MVC.FX.UI module before (refactored from AbstractFXColorPicker, FXColorPicker, FXSimpleGradientPicker, and FXAdvancedGradientPicker). Introduced new FX.JFace module, which provides <code>FXPaintCellEditor</code> and <code>FXPaintSelectionDialog</code>, which were provided by MVC.FX.UI module before (refactored from FXFillCellEditor and FXFillSelectionDialog).

* [#479395](https://bugs.eclipse.org/bugs/show_bug.cgi?id=479395) Merged <code>ScrollPaneEx</code> and <code>FXGridCanvas</code> into a newly created <code>InfiniteCanvas</code>. The InfiniteCanvas does now allow to insert visuals at different "positions" inside of the InfiniteCanvas: underlay group, scrolled underlay group, content group, scrolled overlay group, overlay group.

* [#454681](https://bugs.eclipse.org/bugs/show_bug.cgi?id=454681) Added support to Connection for having a clickable area that is thicker than the connection stroke and allows to select also very thin connections. Its width is exposed by a property of Connection, so it can be bound (e.g. to adjust the width of the clickable area dependent on the scaling). Enhanced the MVC logo example to demonstrate example usage of this feature.

* [#454907](https://bugs.eclipse.org/bugs/show_bug.cgi?id=454907) Ensured that Connection properly clips its start and end decorations.

<img src="/.changelog/Connection_Decoration_Clipping.png" width="300">

* [#488356](https://bugs.eclipse.org/bugs/show_bug.cgi?id=488356) Introduced support for orthogonal routing of a Connection, including support for start and end poin hints. While doing so, separated routing (manipulation of control points) from interpolating (visual appearance), so that routers (straight, orthogonal) and interpolators (poly-line, poly-bezier) can be freely mixed.

<img src="/.changelog/Connection_Snippet.png" width="200">

* Revised DynamicAnchor and its IComputationStrategy to be comparable to a JavaFX binding, where the resulting position value is based on observable computation parameters that might depend on the anchorage (static) or anchored visual (dynamic). This allows to determine the values of computation parameters via JavaFX bindings.

~~~java
getComputationParameter(AnchorageReferenceGeometry.class).bind(new ObjectBinding<IGeometry>() {
  {
    bind(anchorage.layoutBoundsProperty());
  }

  @Override
  protected IGeometry computeValue() {
    return NodeUtils.getShapeOutline(anchorage);
  }
});
~~~

* [#443954](https://bugs.eclipse.org/bugs/show_bug.cgi?id=443954) <code>GeometryNode</code> has been revised to extend Region instead of Parent, so it no longer relies on overriding the <code>impl_computeLayoutBounds()</code> method that is deprecated and announced to be removed in future JavaFX releases. The API of <code>GeometryNode</code> was enhanced so that it does now allow to use <code>relocate(double, double)</code> and <code>resize(double, double)</code> to update the layout bounds (including layoutX and layoutY) while <code>relocateGeometry(double, double)</code> and <code>resizeGeometry(double, double)</code> can be used to update the geometric bounds. The layout bounds (and layoutX, layoutY) resemble the geometric bounds, expanded by a stroke offset (dependent on stroke width and type) and border insets.

### GEF4 MVC (1.0.0)

* [#477352](https://bugs.eclipse.org/bugs/show_bug.cgi?id=477352) The <code>FXTransformPolicy</code> now provides an API for the creation and manipulation of transformation matrices. These matrices are then concatenated, together with the initial host transformation matrix, to yield the new host transformation matrix. Therefore, complex transformations can be set up properly at one point, and the important values can be changed later on, e.g. during user interaction. All transaction and interaction policies related to transforms (<code>FXRelocateConnectionPolicy</code>, <code>FXRelocateOnDragPolicy</code>, <code>FXResizeRelocateOnHandleDragPolicy</code>, <code>FXResizeRelocatePolicy</code>, <code>FXRotatePolicy</code>, <code>FXRotateSelectedOnHandleDragPolicy</code>, <code>FXRotateSelectedOnRotatePolicy</code>, <code>FXScaleRelocateOnHandleDragPolicy</code>, and <code>FXScaleRelocatePolicy</code>) have been renamed to express the actual transformation (e.g. "translate" instead of "relocate"). Additionally, the intermediate <code>FXResizeRelocatePolicy</code>, <code>FXRotatePolicy</code>, and <code>FXScaleRelocatePolicy</code> have been removed. The policies that were dependent on those intermediate policies now directly use the <code>FXTransformPolicy</code>.

* [#479612](https://bugs.eclipse.org/bugs/show_bug.cgi?id=479612) Cleaned up <code>ContentPolicy</code> to only provide operations related to the content of the respective host part. It is intended to be registered at each IContentPart. Moved all "higher-level" operations into <code>CreationPolicy</code> and <code>DeletionPolicy</code>, which are to be registered at the <code>IRootPart</code> alone.

* [#480875](https://bugs.eclipse.org/bugs/show_bug.cgi?id=480875) Updating the selection, hover, and focus models is now handled within the CreationPolicy and the DeletionPolicy and the ClearHoverFocusSelectionOperation has been removed. The CreationPolicy selects and focusses a newly created part. The DeletionPolicy removes the deleted parts from the selection and focus models. Moreover, the CreationPolicy creates the IContentPart before executing operations on the history. Therefore, the content part can be used for live feedback even though the creation is not yet committed.

* [#481600](https://bugs.eclipse.org/bugs/show_bug.cgi?id=481600) Changed the implementation of ContentBehavior to ensure that deactivation does not remove children of its host. This way, deactivation of an FXViewer can now be temporarily applied (to disable all listeners) without loosing the content of the viewer.

* [#488358](https://bugs.eclipse.org/bugs/show_bug.cgi?id=488358) In addition to straight routing based on circle handles, FXBendFirstAnchorageOnSegmentHandleDragPolicy and FXBendConnectionPolicy now support orthogonal routing based on rectangle handles. Analogously to the way point based case, non-filled handle can be used to create new segments, whereas solid handles are used to move segments. Overlay of segments and 'normalization' (i.e. removal of overlaid segments) are performed during interaction.

<img src="/.changelog/Straight_Routing_PolyBezier_Interpolator.png" width="250">
<img src="/.changelog/Orthogonal_Routing_Polyline_Interpolator.png" width="250">

* [#493553](https://bugs.eclipse.org/bugs/show_bug.cgi?id=493553) IDomain now accepts only ITransactionalOperations to execute. This allows to filter non content-related operations (like selection of focus change) in case only content-relevant changes should be undoable. 

* [#481688](https://bugs.eclipse.org/bugs/show_bug.cgi?id=481677) The MVC Logo example has been augmented by a fly-out palette for object creation, to demonstrate how multiple viewers can be combined in a single domain.

<img src="/.changelog/Palette_for_object_creation_001.png" width="250">
<img src="/.changelog/Palette_for_object_creation_002.png" width="250">
<img src="/.changelog/Palette_for_object_creation_003.png" width="250">
<img src="/.changelog/Palette_for_object_creation_004.png" width="250">
<img src="/.changelog/Palette_for_object_creation_005.png" width="250">

### GEF4 Layout (1.0.0)

* [#491097](https://bugs.eclipse.org/bugs/show_bug.cgi?id=491097) The LayoutContext and ILayoutAlgorithm now use a Graph data model as input and output model for layout calculations instead of its own layout interface abstractions.

~~~java
Graph graph = ...
layoutContext.setGraph(graph);  
layoutContext.setLayoutAlgorithm(layoutAlgorithm);
layoutContext.applyLayout(true);
~~~

### GEF4 Graph (1.0.0)

* [#480293](https://bugs.eclipse.org/bugs/show_bug.cgi?id=480293) Enhanced the builder API so that graph-, node-, and edge-builders can be chained appropriately. Node-builders can now be provided with an Object-key, that can be referred to by edge-builders.

~~~java
Graph g1 = new Graph.Builder()
.attr(DotAttributes._TYPE__G, DotAttributes._TYPE__G__DIGRAPH)//
.node("n1")//
.attr(DotAttributes.LABEL__GNE, "1")//
.attr(DotAttributes.ID__GNE, "1")//
.node("n2")//
.attr(DotAttributes.LABEL__GNE, "2")//
.attr(DotAttributes.ID__GNE, "2")//
.node("n3")//
.attr(DotAttributes.LABEL__GNE, "3")//
.attr(DotAttributes.ID__GNE, "3")//
.edge("n1", "n2")//
.edge("n1", "n3")//
.build();
~~~

* [#484774](https://bugs.eclipse.org/bugs/show_bug.cgi?id=484774) All relevant properties are now exposed as JavaFX observable collections/properties.

### GEF4 Zest (1.0.0)

* [#470636](https://bugs.eclipse.org/bugs/show_bug.cgi?id=470636) Separated out the Zest JFace-API into an own module (Zest.FX.JFace), so it can be consumed without introducing dependencies on the Eclipse Workbench UI.

* [#478944](https://bugs.eclipse.org/bugs/show_bug.cgi?id=478944) Removed IEdgeDecorationProvider from Zest.FX.JFace. Respective ZestProperties.EDGE_SOURCE_DECORATION and ZestProperties.EDGE_TARGET_DECORATION attributes may be provided via IGraphNodeLabelProvider#getEdgeAttributes() instead.

~~~java
class MyLabelProvider extends LabelProvider  implements IGraphAttributesProvider {	
  @Override
  public Map<String, Object> getEdgeAttributes(Object sourceNode, Object targetNode) {
    Map<String, Object> edgeAttributes = new HashMap<>();
    edgeAttributes.put(ZestProperties.SOURCE_DECORATION__E, new CircleHead());
    edgeAttributes.put(ZestProperties.TARGET_DECORATION__E, new DiamondHead());
    edgeAttributes.put(ZestProperties.CURVE_CSS_STYLE__E, "-fx-stroke: red;");
    return edgeAttributes;
  }
  ...
}
~~~

### GEF4 DOT (1.0.0)

The DOT component has been completely revised and has made significant progress towards a full DOT authoring environment. It does not provide public API yet and is currently limited to provide end-user functionality.

* [#446639](https://bugs.eclipse.org/bugs/show_bug.cgi?id=446639) A Graphviz preference page has been added, via which the path to the native dot executable and a Graphviz export format can be specified.

<img src="/.changelog/DOT_Graphviz_preference_page.png" width="400">

* [#337644](https://bugs.eclipse.org/bugs/show_bug.cgi?id=337644) A 'Sync Graphviz Export' toggle has been added as a DOT editor action to the main toolbar (it was previously located in the DOT Graph view). If enabled, it will export the persisted state of a currently edited DOT file in the format specified in the Graphviz preferences.

* The DOT Graph view will now use the native dot executable (if specified via the preferences) for layout (native mode), while it will otherwise use a comparable Layout algorithm (emulated mode).

<img src="/.changelog/DOT_Graph_view_native.png" width="400">
<img src="/.changelog/DOT_Graph_view_emulated.png" width="400">

* [#477980](https://bugs.eclipse.org/bugs/show_bug.cgi?id=477980) The DOT Graph view now also provides additional rendering capabilities, including edge decorations, splines, as well as all available kind of labels (for edges and nodes). 

<img src="/.changelog/DOT_Graph_view_arrowtypes.png" width="200">
<img src="/.changelog/DOT_Graph_view_splines.png" width="180">
<img src="/.changelog/DOT_Graph_view_styles.png" width="650">

* The DOT editor has been enhanced with additional validation support for arrowtype, point, splinetype, shape, and style attribute values.

<img src="/.changelog/DOT_Editor_warnings.png" width="550">
<img src="/.changelog/DOT_Editor_errors.png" width="550">

# [GEF 3.10.1 (Mars.1)](https://projects.eclipse.org/projects/tools.gef/releases/3.10.1-mars.1)
Update release providing minor revisions (0.2.0) of the preliminary GEF4 components.

Please note that some minor adjustments have been applied to the provisional API of GEF4. The list of added and removed classes can be found at [GEF4 0.1.0-0.2.0 API Diff](https://www.eclipse.org/gef/project-info/GEF4-0.1.0-0.2.0-Provisional-API-Diff.html). The most notable API changes are outlined below.

### GEF4 MVC (0.2.0)

* [#472649](https://bugs.eclipse.org/bugs/show_bug.cgi?id=472649), [#472650](https://bugs.eclipse.org/bugs/show_bug.cgi?id=472650) Removed <code>getContents()</code> callback from <code>FXView</code> and <code>FXEditor</code>, so that population of viewers is now completely left to subclasses. Modularized <code>createPartControl()</code> and <code>dispose</code> methods so clients can easily overwrite and adopt individual aspects. In detail, introduced <code>hookViewers()</code>, <code>unhookViewers()</code>, <code>activate()</code>, and <code>deactivate()</code> hook methods.

* [#472646](https://bugs.eclipse.org/bugs/show_bug.cgi?id=472646) Fixed that multiple FXViewers could not share a JavaFX scene. FXViewer was responsible of creating a JavaFX scene and was provided with an ISceneContainer implementation, to hook the scene into a Stage (standalone) or FXCanvas (Eclipse UI integration) as follows:
~~~java
// standalone
viewer.setSceneContainer(new FXStageSceneContainer(primaryStage));

// Eclipse UI integration
viewer.setSceneContainer(new FXCanvasSceneContainer(canvas));
~~~
The FXViewer had full control over the scene creation, so other root visuals than those provided by the viewer could not be set for the scene. This responsibility was moved out of FXViewer, so its visuals are now hooked into the scene as follows:
~~~java
// standalone
primaryStage.setScene(new Scene(viewer.getScrollPane()));

// Eclipse UI integration
canvas.setScene(new Scene(viewer.getScrollPane()));
~~~
Thereby, several <code>FXViewers</code> can now share a single scene, which is e.g. necessary to create a palette viewer. By means of an JavaFX <code>SplitPane</code>, this could look like follows:
~~~java
// embed two viewers into a single scene by means of a SplitPane
SplitPane sp = new SplitPane();
sp.getItems().addAll(viewer1.getScrollPane(), viewer2.getScrollPane());
sp.setDividerPositions(0.5f);
primaryStage.setScene(new Scene(sp));
~~~
The now obsolete <code>org.eclipse.gef4.mvc.fx.viewer.ISceneContainer</code> abstraction and the related <code>org.eclipse.gef4.mvc.fx.FXStageSceneContainer</code>, and <code>org.eclipse.gef4.mvc.fx.ui.FXCanvasSceneContainer</code> implementations were removed. 

* [#472650](https://bugs.eclipse.org/bugs/show_bug.cgi?id=472650) Enabled that multiple viewers can easily be used with an <code>FXView</code> or <code>FXEditor</code> by factoring out <code>hookViewers()</code> and <code>populateViewers()</code> hook methods from <code>createPartControl()</code>, with a default implementation that is based on a single default viewer, which can easily be overwritten by subclasses to hook/populate multiple viewers.

* [#471031](https://bugs.eclipse.org/bugs/show_bug.cgi?id=471031) Enhanced customizing of the default resize mechanism by allowing to overwrite the identification of the to-be-resized visual (only the part's "main" visual was allowed before) as well as the initial size (the visual's layout-bounds were used before).

* [#470612](https://bugs.eclipse.org/bugs/show_bug.cgi?id=470612) Added checks to determine the dirty state of an <code>FXEditor</code> based on the undo-history.

* [#469491](https://bugs.eclipse.org/bugs/show_bug.cgi?id=469491) Added a widget for creating/manipulating multi-stop linear gradients:

<img src="/.changelog/FXFillSelectionDialog.png">

### GEF4 Zest (0.2.0)

* [#466815](https://bugs.eclipse.org/bugs/show_bug.cgi?id=466815) Renamed <code>org.eclipse.gef4.zest.fx.models.ViewportStackModel</code> to <code>org.eclipse.gef4.zest.fx.models.NavigationModel</code> and refactored it basically. Introduced <code>org.eclipse.gef4.zest.fx.policies.NavigationPolicy</code>, refactoring those parts of the semantic zooming that depended on the ViewportStackModel directly to use the new NavigationPolicy instead.

### GEF4 Cloudio (0.2.0)

* [#473695](https://bugs.eclipse.org/bugs/show_bug.cgi?id=473695) Made <code>IEditableCloudLabelProvider</code> and <code>CloudOptionsComposite</code> internal by moving them to an internal package.

# [GEF 3.10.0 (Mars)](https://projects.eclipse.org/projects/tools.gef/releases/3.10.0-mars)
Annual release providing first (preliminary) snapshot (0.1.0) of the new GEF4 components. The most significant changes, implemented during the Mars release timeframe, is depicted per component in the following.

### GEF4 Common (0.1.0)

The Common component has been created by extracting generic abstractions (e.g. <code>IActivatable</code> or <code>IAdaptable</code>) and supporting classes from the FX and MVC components. It provides functionality that is (potentially) used by all other components and was basically written from scratch.

* To combine the <code>IAdaptable</code>-mechanism with Google Guice-based dependency injection, a specific map-binding was introduced, which allows to inject adapters into an <code>IAdaptable</code> (see ['IAdaptable - GEF4's interpretation of a classic'](http://nyssen.blogspot.de/2014/11/iadaptable-gef4s-interpretation-of.html) for details). 

* [#453119](https://bugs.eclipse.org/bugs/show_bug.cgi?id=453119) Augmented <code>IAdaptable</code> to enable registration and retrieval of adapters based on <code>TypeToken</code> keys. This way, registration of adapters can not only be performed based on raw types (e.g. <code>Provider.class</code>) but also using parameterized types (e.g. <code>Provider<IGeometry></code> or <code>Provider<IFXAnchor></code>) (see ['IAdaptable - GEF4's interpretation of a classic'](http://nyssen.blogspot.de/2014/11/iadaptable-gef4s-interpretation-of.html) for details). 

* [#458320](https://bugs.eclipse.org/bugs/show_bug.cgi?id=458320) Implemented a specific Guice <code>Scope</code> (<code>AdaptableScope</code>) that allows to (transitively) scope instances to <code>IAdaptable</code>s.

### GEF4 Geometry (0.1.0)

The Geometry component had been written from scratch to provide a decent double-based geometry API before the Mars release timeframe. It was only marginally extended within.

* Extended the <code>ICurve</code> interface with a method (<code>getNearestIntersection(ICurve, Point)</code>) that allows to compute the intersection point with another <code>ICurve</code> nearest to some reference point. This is e.g. quite handy when computing anchor position within <code>FXChopBoxAnchor</code>.

### GEF4 Graph (0.1.0)

The Graph component has been factored out of the former [Zest2](https://wiki.eclipse.org/Tree_Views_for_Zest) code base (DOT4Zest).

* Changed that a constructed <code>Graph</code> is immmutable. The limitation of read-only access had prevented use cases of Zest and DOT, where a constructed <code>Graph</code> had to be modified after its initial construction.

* Added support for nested graphs, so that a <code>Node</code> can now refer to a nested <code>Graph</code>. The node containing the nested graph is referred to as the 'nesting node'.

* The <code>GraphCopier</code> that was initially bundled with the DOT component as <code>ZestGraphImport</code> was moved to Graph, as it provides generic capabilities to copy <code>Graph</code> instances and is not bound to Zest or DOT.

### GEF4 Layout (0.1.0)

The Layout component has been factored out of the former [Zest2](https://wiki.eclipse.org/Tree_Views_for_Zest) code base (<code>org.eclipse.gef4.zest.layouts</code>), which was initially created by forking [Zest 1.x](https://www.eclipse.org/gef/zest) (<code>org.eclipse.zest.layouts</code>). It has been significantly refactored within this process.

* Moved the Sugiyama-specific abstractions (<code>LayerProvider</code>, <code>NodeWrapper</code>, <code>CrossingReducer</code>) into the <code>SugiyamaLayoutAlgorithm</code> as nested classes. Removed the <code>ExpandCollapseManager</code> from the <code>LayoutContext</code>, as it is specific to the <code>SpaceTreeLayoutAlgorithm</code>.

### GEF4 FX (0.1.0)

The FX component has been written from scratch to replace the [Draw2d 3.x](https://eclipse.org/gef/draw2d/index.php) legacy component. It uses JavaFX for visualization and provides support for integrating JavaFX into SWT. A lightweight rendering support for SWT (as provided by Draw2d is not part of this component). 

* [#441463](https://bugs.eclipse.org/bugs/show_bug.cgi?id=441463) Removed the need for specific SwtFXScene implementation by ensuring that <code>FXControlAdapter</code> (formerly <code>SwtFXControlAdapter</code>) can work with an arbitrary JavaFX <code>Scene</code>.

* [#442971](https://bugs.eclipse.org/bugs/show_bug.cgi?id=442971), [#444009](https://bugs.eclipse.org/bugs/show_bug.cgi?id=444009) Revised anchor abstraction so that <code>IFXAnchor#attach()</code> and <code>IFXAnchor#detach()</code> now take an additional <code>IAdaptable</code> argument, which may be used to provide additional information to the specific anchor. In case of an <code>FXChopBoxAnchor</code>, this mechanism is used to pass in a <code>FXChopBoxAnchor$ReferencePointProvider</code>, thereby replicating the old <code>FXChopBoxHelper</code> mechanism which involved direct coupling. Furthermore, an <code>FXChopBoxAnchor$ComputationStrategy</code> interface has been extracted from the <code>FXChopBoxAnchor</code> (an implementation can be passed in via its constructor), so the strategy, which is used to compute anchor positions, can be replaced.

* [#443781](https://bugs.eclipse.org/bugs/show_bug.cgi?id=443781) An <code>IFXConnectionRouter</code> interface has been extracted from <code>FXConnection</code>. It can be passed in via <code>FXConnection#setRouter()</code> and is responsible of computing a curve geometry from the passed in (manually provided) waypoints of the connection.

* Introduced ScrollPaneEx, which is an alternative to JavaFX's ScrollPane. The ScrollPaneEx provides a set-up which is suitable for graphical viewers/editors, i.e. an "infinite" canvas with viewport transformation, <code>reveal(Node)</code> functionality, fully controllable behavior, etc.

### GEF4 MVC (0.1.0)

The MVC component has been written from scratch to replace the [GEF (MVC) 3.x](https://eclipse.org/gef/gef_mvc/index.php) legacy component. While some proven concepts have been adopted, the component is a complete re-design.

 * Added grid layer and implemented snap-to-grid support. The new grid layer is created as an underlying layer by default. Visibility of the layer can be controlled via a <code>GridModel</code>, which is to be registered as an <code>IViewer</code> adapter. The <code>GridModel</code> allows to select the grid cell height and width and whether the grid is to be zoomed with the contents layer or not. It can also be used to enable/disable snap-to-grid, which is respected by the <code>FXResizeRelocatePolicy</code> and <code>FXBendPoliy</code>.

<img src="/.changelog/MVC_Grid.png" width="250">

* To enable that handles are only displayed during mouse hover (and a short delay after), <code>FXHoverBehavior</code> and the <code>FXDefaultHandlePartFactory</code> now support the creation (and automatic removal) of hover handles. 

<img src="/.changelog/MVC_Hover_Handles.png" width="250">

* The <code>IDomain</code> now allows to open/close transactions, executing a set of related <code>IUndoableOperation</code> as an atomic (composite) operation. This way, several (transaction) policies can independently execute operations during an interaction, while undo/redo is performed for the overall transaction.

* Introduced <code>ContentPolicy</code> to formalize that parts of the interaction that affects the content (i.e. the to be visualized model). The content <code>ContentPolicy</code> provides undoable operations to add child content to a parent, to anchor content on other content, etc. All these operations delegate to the respective host <code>IContentPart</code>. As the respective content parts have to be implemented by adopters anyway, the model visualization and model manipulation related code can be kept in a single place (namely within the <code>IContentPart</code>). Furthermore, the <code>ContentPolicy</code> is generic and can be re-used independently.

* [#449129](https://bugs.eclipse.org/bugs/show_bug.cgi?id=449129), [#462787](https://bugs.eclipse.org/bugs/show_bug.cgi?id=462787) Added support for touch gesture-based interactions. The MVC logo example demonstrates gesture-based panning/scrolling, zooming, and rotating.

### GEF4 Zest (0.1.0)

The Zest component has been basically re-written from scratch to replace the [Zest 1.x](https://www.eclipse.org/gef/zest) legacy component, as well as the intermediary replacement in the form of the [Zest2](https://wiki.eclipse.org/Tree_Views_for_Zest) code base (<code>org.eclipse.gef4.zest.core</code> and <code>org.eclipse.gef4.zest.jface</code>), which was created by forking [Zest 1.x](https://www.eclipse.org/gef/zest) (<code>org.eclipse.zest.core</code>). It is now based on the Graph and Layout components, which were factored out of the same code base, and uses MVC as the underlying model-view-controller framework.

* [#438734](https://bugs.eclipse.org/bugs/show_bug.cgi?id=438734) Re-implemented the functionality of zest.core within ZEST.FX and Zest.FX.UI.

* [#441131](https://bugs.eclipse.org/bugs/show_bug.cgi?id=441131) Created a replacement API for zest.jface in terms of the  <code>ZestContentViewer</code>. While custom content and "label" providers are defined within <code>org.eclipse.gef4.zest.fx.ui.jface</code>, the JFace <code>ILabelProvider</code>, <code>IColorProvider</code>, <code>IFontProvider</code>, and <code>IToolTipProvider</code> are also accepted.

* Nodes can now be hidden using the '-' hover handle on the node. Sibling nodes, connected to the hidden node will indicate the number of hidden nodes, connected to them, by means of a (red circle) decoration. Using the '+' hover handle on any such sibling node, will make the hidden nodes visible again.

* Implemented inline-rendering of nested graphs. Nesting nodes will now render their nested nodes at a certain zoom threshold. Zooming can be performed with the mouse wheel and 'Alt' or 'Ctrl' key, or by using a pinch-spread gesture (on devices which support touch gestures).

<img src="/.changelog/Zest_Inline_Rendering_Nested_Graphs.png" width="400">

* Implemented navigating to/from nested graphs. Only one level of a hierarchy is rendered as once, but you can open a nested graph by double clicking the corresponding node. Navigating is further supported by using zoom. When zooming a nesting node further, from a certain zoom threshold onwards, the viewer contents will be switched to render only the nested graph. Zooming out will return to the above graph level.

### GEF4 DOT (0.1.0)

The DOT component has been factored out of the [Zest2](https://wiki.eclipse.org/Tree_Views_for_Zest) code base (DOT4Zest) and has been completely revised to be internally  based on the new Zest component.

* [#441129](https://bugs.eclipse.org/bugs/show_bug.cgi?id=441129) Replacing the former Zest2 code within <code>org.eclipse.gef4.zest.ui</code> that still depended on [Draw2d 3.x](https://eclipse.org/gef/draw2d/index.php), a new DOT <code>Graph</code> view was introduced, which is based on Zest and thus uses JavaFX for visualization purposes.

<img src="/.changelog/DOT_Graph_View_initial.png" width="400">

* [#451097](https://bugs.eclipse.org/bugs/show_bug.cgi?id=451097) Refactored grammar definition and fixed several parsing issues within DOT editor.

* [#450448](https://bugs.eclipse.org/bugs/show_bug.cgi?id=450448) Add proper support for lexical and semantic syntax coloring within DOT editor.

* [#452650](https://bugs.eclipse.org/bugs/show_bug.cgi?id=452650) Implemented a proper outline view for the DOT editor.
<img src="/.changelog/DOT_Editor_initial.png" width="500">

### GEF4 Cloudio (0.1.0)

The Cloudio component has been factored out of the [Zest2](https://wiki.eclipse.org/Tree_Views_for_Zest) code base (Cloudio) into a standalone component. It does not rely on other GEF4 components and was not yet migrated to use JavaFX for rendering.