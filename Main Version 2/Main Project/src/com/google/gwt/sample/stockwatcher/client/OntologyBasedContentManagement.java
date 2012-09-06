package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.TreeViewModel.NodeInfo;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;
import com.google.gwt.cell.client.TextCell;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class OntologyBasedContentManagement implements EntryPoint {
	private Anchor new_page = new Anchor("Ontology Tree");
	private Anchor home_page = new Anchor("Home");
	private Anchor to_content = new Anchor("To Content");
	private Anchor download_repository = new Anchor("Download Repository");
	private String export_fp = "";
	private boolean repository_downloaded = false;
	// private ColumnModel cm = new ColumnModel(Stocks);
	/*
	 * Create interface instances
	 */
	private DialogBox dBox = new DialogBox(false);
	private PopupPanel popup = new PopupPanel();
	private HTML message;
	private CheckBox cb;
	private Tree browseTree = new Tree();

	private String baseURI;
	private String url = "http://www.cngl.ie";
	private String ontName;
	private ScrollPanel grid_scroll = new ScrollPanel(); // page 2, instances

	private VerticalPanel mainPanel = new VerticalPanel(); // Holds the table
	private VerticalPanel secondPanel = new VerticalPanel();
	private VerticalPanel radioButtonPanel = new VerticalPanel();
	private VerticalPanel dialogBoxContents = new VerticalPanel();
	private VerticalPanel popupContents = new VerticalPanel();
	private VerticalPanel instance_link = new VerticalPanel();
	private VerticalPanel queryPanel = new VerticalPanel(); // holds panel for
															// querying
	private VerticalPanel bottomOfScreen = new VerticalPanel(); // Subject
																// search
	// & triple table
	private VerticalPanel page2Panel = new VerticalPanel();
													// Ontology
	private FlexTable tripleTable = new FlexTable(); // table for hold triples
														// to be sent
	private FlexTable ft = new FlexTable();
	private FlexTable instance_grid = new FlexTable();

	private HorizontalPanel addPanel = new HorizontalPanel();
	private HorizontalPanel searchPanel = new HorizontalPanel(); // find word in
																	// content
	private HorizontalPanel loadOntologyInternet = new HorizontalPanel();
	private HorizontalPanel suggested_checkbox = new HorizontalPanel();
	private HorizontalPanel uploadedOntologies = new HorizontalPanel();
	private HorizontalPanel tree_grid = new HorizontalPanel();

	private SimplePanel dialogBoxholder = new SimplePanel();
	private SimplePanel popupHolder = new SimplePanel();
	// private SimplePanel contextHolder = new SimplePanel();
	private TextBox entercontext = new TextBox();
	private TextBox content = new TextBox(); // search webpage for content
	private TextBox webBox = new TextBox();
	private TextBox ontology_from_disk = new TextBox();
	private TextBox subjectQuery = new TextBox();
	private TextBox contextQuery = new TextBox();
	private TextBox link = new TextBox();
	private TextBox ontology_from_internet = new TextBox(); // enter URL of
															// ontology
	private TextBox user_enter_subject = new TextBox(); // user enter word not

	private Button search = new Button(); // button to trigger search
	private Button webSend = new Button();
	private Button save = new Button(); // saves selected triples to arraylist
	private Button load_ontology_web_button = new Button();
	private Button user_subject_button = new Button();
	private Button close = new Button();
	private Button closePopup = new Button("Close");
	private Button loadFile = new Button("Load File");
	// private Button suggestionButton = new Button("Suggestions");

	private RadioButton radioA = new RadioButton("group", "Subject");
	private RadioButton radioB = new RadioButton("group", "Object");

	private Label update = new Label();
	private ListBox ontology_Classes, ontologies;
	private Frame frame; // uses HTTPRequest to get website
	private String frameWidth, frameHeight;
	private String subject = "";
	private String path_of_uploaded_file = "";
	private int selectedListIndex = -1;
	private int row = 1; // keeps track of rows in flex table
	private ArrayList<String> Stocks = new ArrayList<String>(); // from tutorial
																// ignore
	private ArrayList<String> list = new ArrayList<String>(); // To be sent to
																// server
	private ArrayList<String> classes = new ArrayList<String>();
	private ArrayList<String> properties = new ArrayList<String>();
	private ArrayList<String> literals = new ArrayList<String>();
	private List<String[]> sugT = new ArrayList<String[]>();
	private ArrayList<String[]> insert_ToTable = new ArrayList<String[]>(); // suggested
																			// triples

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class); // make
	private static Logger logger = Logger.getLogger("");
	/*
	 * file instances
	 */
	private String filename = "";
	private static final String LOADING_ITEMS = "Loading items. . .";
	private FileUpload fileUpload = new FileUpload();
	// private MultiUploader mu = new MultiUploader();
	private FormPanel form = new FormPanel();
	private Label statusLabel = new Label();
	private static String filepathofexport;
	// private Ontology ontology;
	private ArrayList<Ontology> ontology = new ArrayList<Ontology>();
	private int left = (int) (Window.getClientWidth() / 1.5), top = (int) Window.getClientHeight() / 4;
	private String link_to_content_page;
	private int rowIndex = 0;

	private MenuBar Properties = new MenuBar(false);
	private MenuBar PropertyMenu = new MenuBar(true);
	private MenuItem ontProperties = new MenuItem("Object", false, PropertyMenu);
	private MenuBar adding_data_prop = new MenuBar(true);
	private MenuItem ontData = new MenuItem("Data", false, adding_data_prop);
	private MenuBar adding_object_prop = new MenuBar(true);
	private MenuItem ontObject = new MenuItem("Pre", false, adding_object_prop);
	private MenuBar Classes = new MenuBar(false);
	private MenuBar adding_objects = new MenuBar(true);
	private MenuItem ontClasses = new MenuItem("Object", false, adding_objects);

	/*
	 * Entry Point method.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		/*
		 * Create file interface
		 */
		// Create a FormPanel and point it at a service.
		form = new FormPanel();
		form.setAction(GWT.getModuleBaseURL() + "greet");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		form.setWidget(secondPanel);
		uploadedOntologies.add(loadFile);
		ClickHandler load_handler = new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				form.submit();
			}
		};
		loadFile.addClickHandler(load_handler);

		ontologies = new ListBox();
		ontologies.setSize("152px", "18px");

		uploadedOntologies.add(ontologies);
		uploadedOntologies.setSpacing(5);
		// secondPanel.add(uploadedOntologies);

		/*
		 * end file creation
		 */
		logger.log(Level.SEVERE, "Log!");
		double wi = Window.getClientWidth() / 3.5;
		String tablewidth = Double.toString(wi);
		frameWidth = String.valueOf(Window.getClientWidth() / 3.3) + "px";
		ontology_from_disk.setText("/Users/markhender/ontologies/pizzas/pizza.rdf");
		ontology_from_disk.setWidth("340px");
		frame = new Frame();
		frame.setUrl(url);
		frameWidth = String.valueOf(Window.getClientWidth() / 1.5) + "px"; // works
																			// cross
																			// browsers
		frameHeight = String.valueOf(String.valueOf(Window.getClientHeight() / 1.3) + "px");
		frame.setSize("1163px", "461px");
		frame.setVisible(true);

		/*
		 * Popup Panel
		 */
		popupHolder.add(closePopup);

		popup.setWidget(popupContents);
		closePopup.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int end_of_list = ft.getRowCount();
				int count = 1;

				while (count < end_of_list) {
					logger.log(Level.SEVERE, "line");
					CheckBox box = (CheckBox) ft.getWidget(count, 3);
					if (box.getValue()) {
						// tripleTable.setText(tripleTable.getRowCount(), 0,
						// ft.getText(count, 0));
						printSuggestedSubject(ft.getText(count, 0));
						addPredicate(ft.getText(count, 1));
						if (ft.getText(count, 1).endsWith("*"))
							addLitObject(ft.getText(count, 2));
						else
							addObject(ft.getText(count, 2));
					}
					count++;
				}
				logger.log(Level.SEVERE, "BINGO");
				ft.removeAllRows();

				popup.hide();
				popupContents.clear();
				popupContents.add(popupHolder);
			}
		});
		content.setText("Search for content in webpage");
		content.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				content.setFocus(true);
				if (content.getText().equals("Search for content in webpage"))
					content.setText("");
				else
					content.selectAll();
			}
		});

		searchPanel.add(content); // content search box
		searchPanel.add(search); // trigger content search button
		search.setHeight("37px");
		dBox.setText("Triple Report");
		close.setText("Close");
		close.addClickListener(new ClickListener() {

			@Override
			public void onClick(Widget sender) {
				dialogBoxContents.clear();
				dBox.hide();
			}
		});
		dialogBoxholder.add(close);
		radioButtonPanel.add(radioA);
		radioButtonPanel.add(radioB);
		searchPanel.add(radioButtonPanel);
		bottomOfScreen.add(searchPanel);

		// tripleTable.setWidget(0, 2, menuBar_3);
		// menuBar_3.addItem(Classes);
		// bottomOfScreen.setSpacing(10);
		search.setText("Enter");
		content.setSize("282px", "29px");
		ListBox listbox = new ListBox();
		ontology_Classes = new ListBox();
		ontology_Classes.setWidth("100px");

		listbox.setWidth("100px");
		listbox.setHeight("400px");
		save.setText("Save");
		listbox.setVisible(false);
		/*
		 * before new page
		 */
		final PopupPanel contextpanel = new PopupPanel();

		final Button ok = new Button("OK");
		final HorizontalPanel hori = new HorizontalPanel();
		contextpanel.setWidget(hori);
		entercontext.setText("context");
		hori.add(ok);
		final ClickHandler download_handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				greetingService.downloadRepository(entercontext.getText(), new downloadRepository());
				if (repository_downloaded)
					loadPageTwo(export_fp);
				repository_downloaded = true;
				logger.log(Level.SEVERE, "download_handler called");
			}
		};
		ClickHandler newpage_handler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				contextpanel.center();
				ok.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (repository_downloaded)
							loadPageTwo(export_fp);
						else {
							download_handler.onClick(event);

						}
						contextpanel.hide();

					}

				});
				logger.log(Level.SEVERE, "export path: " + filepathofexport);

			}

		};
		new_page.addClickHandler(newpage_handler);
		download_repository.addClickHandler(download_handler);
		/* Return to homepage */
		home_page.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadHomePage();
			}
		});
		/* home page */

		// mainPanel.add(frame); // browser
		mainPanel.add(uploadedOntologies);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		mainPanel.add(new_page);
		mainPanel.add(download_repository);

		RootPanel rootPanel = RootPanel.get("stockList");
		rootPanel.add(frame, 4, 132);
		RootPanel.get("stockList").add(bottomOfScreen, 10, 829);
		bottomOfScreen.setSize("440px", "45px");
		RootPanel.get("stockList").add(form, frame.getOffsetWidth() + 10, frame.getAbsoluteTop());
		RootPanel.get("stockList").add(mainPanel, 284, 753);
		mainPanel.setSize("236px", "70px");
		frame.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {

			}

		});
		final AsyncCallback<ArrayList<String>> ontology_class = new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				classes = result;
				ontology.get(ontology.size() - 1).setClasses(result);
				if (ontology.size() == 1)
					populate_ClassBox(0);
			}
		};
		final AsyncCallback<ArrayList<String>> property_resource = new AsyncCallback<ArrayList<String>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				properties = result;
				ontology.get(ontology.size() - 1).setProperties(result);
				if (ontology.size() == 1)
					populate_PropertyBox(0);
			}
		};
		final AsyncCallback<ArrayList<String>> property_literal = new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				literals = result;
				ontology.get(ontology.size() - 1).setLiterals(result);
				if (ontology.size() == 1)
					populate_LiteralBox(0);
			}
		};
		final AsyncCallback<Integer[]> subjectIndexOfContent = new AsyncCallback<Integer[]>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Integer[] result) {
				if (result[0] != -99 && result[1] != -99) {
					// word found
					printSubject();
				} else
					Window.alert("Word not found");
			}

		};

		final AsyncCallback<String> getOntName = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(String result) {
				ontName = result;
				ontology.get(ontology.size() - 1).setName(result);
				logger.log(Level.SEVERE, ("OntologyName = " + ontName));
			}

		};

		final AsyncCallback<String> geturi = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(String result) {
				baseURI = result;
				ontology.get(ontology.size() - 1).setBaseURI(result);
				logger.log(Level.SEVERE, "The baseURI is " + baseURI);
			}

		};
		final AsyncCallback<ArrayList<String[]>> suggestedTriples = new AsyncCallback<ArrayList<String[]>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<String[]> result) {
				logger.log(Level.SEVERE, "First");
				populateSuggestedTriples(result);
				Window.alert("Pass");
			}

		};

		search.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String blah = content.getText();
				if (radioA.isChecked() || !radioB.isChecked())
					greetingService.wordExists(blah, webBox.getText(), subjectIndexOfContent);
				else {
					printSubject();
				}
				content.setFocus(true);
				content.selectAll();
			}
		});

		content.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {

				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if (radioA.isChecked() || !radioB.isChecked())
						greetingService.wordExists(content.getText(), webBox.getText(), subjectIndexOfContent);
					else
						printSubject();
					content.setFocus(true);
					content.selectAll();
				}
			}
		});
		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String[] temp = new String[3];
				temp = getTriples();
				repository_downloaded = false;
			}
		});
		rootPanel.add(fileUpload, 283, 717);
		fileUpload.setSize("237px", "30px");
		// secondPanel.add(statusLabel);
		// fileUpload.setName(form.getTitle());
		fileUpload.setName("UploadFile");
		rootPanel.add(addPanel, 10, 93);
				addPanel.add(webBox);
		
				webBox.setText(url);
				webBox.setWidth("340px");
				addPanel.add(webSend);
				webSend.setSize("39px", "33px");
				
						webSend.setText("GO");
								rootPanel.add(loadOntologyInternet, 10, 738);
								loadOntologyInternet.setSize("267px", "36px");
						
								loadOntologyInternet.add(ontology_from_internet);
								ontology_from_internet.setSize("204px", "26px");
								load_ontology_web_button.setHTML("Load");
								loadOntologyInternet.add(load_ontology_web_button);
								load_ontology_web_button.setSize("53px", "37px");
								rootPanel.add(tripleTable, 10, 889);
								tripleTable.setStyleName("Prompt-User");
								tripleTable.getRowFormatter().addStyleName(0, "Prompt-User");
								tripleTable.getColumnFormatter().addStyleName(0, "columnOne");
								tripleTable.getColumnFormatter().addStyleName(1, "columnTwo");
								tripleTable.getColumnFormatter().addStyleName(2, "columnThree");
								tripleTable.addStyleName("tripleTable");
								row = tripleTable.getRowCount();
								tripleTable.setSize("1038px", "67px");
								tripleTable.setText(0, 0, "SUBJECT");
								tripleTable.setWidget(0, 1, Properties);
								Properties.setSize("100%", "100%");
								ontProperties.setHTML("Predicate");
								PropertyMenu.setSize("100%", "100%");
								PropertyMenu.addItem(ontData);
								ontObject.setHTML("Object");
								PropertyMenu.addItem(ontObject);
								Properties.addItem(ontProperties);
								ontProperties.setSize("100%", "100%");
								
										MenuBar Classes_1 = new MenuBar(false);
										tripleTable.setWidget(0, 2, Classes_1);
										Classes_1.setHeight("100%");
										Classes_1.addItem(ontClasses);
										tripleTable.addClickHandler(new ClickHandler() {
											@Override
											public void onClick(ClickEvent event) {
												com.google.gwt.user.client.ui.HTMLTable.Cell cell = tripleTable.getCellForEvent(event);
												int cellIndex = cell.getCellIndex();
												int rowIndex = cell.getRowIndex();
												if (cellIndex == 4 || cellIndex == 0) {
													logger.log(Level.SEVERE, "Sending: " + tripleTable.getText(rowIndex, 0));
													greetingService.suggestedTriples(tripleTable.getHTML(rowIndex, 0), suggestedTriples);

												}

											}
										});
								
										load_ontology_web_button.addClickHandler(new ClickHandler() {
								
											@Override
											public void onClick(ClickEvent event) {
												// TODO load ontologies from a web address
											}
								
										});
						
								// Listen for mouse events on webSend Button
								// to get new website
								webSend.addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										frame.setUrl(webBox.getText()); // take url from textbox
										logger.log(Level.SEVERE, frame.getUrl());
										content.setFocus(true);
										content.selectAll();
									}
								});
				// listen for keyboard events in the textbox
				webBox.addKeyUpHandler(new KeyUpHandler() {
					@Override
					public void onKeyUp(KeyUpEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
							frame.setUrl(webBox.getText());
							content.setFocus(true);
							content.selectAll();
						}
					}
				});

		/*
		 * Adding Change listener to listboxes
		 */
		ontologies.addChangeListener(new ChangeListener() {
			@Override
			public void onChange(Widget sender) {
				int listIndex = ontologies.getSelectedIndex();

				populate_ClassBox(listIndex);
				populate_PropertyBox(listIndex);
				populate_LiteralBox(listIndex);
			}
		});
		ontology_Classes.addChangeListener(new ChangeListener() {

			@Override
			public void onChange(Widget sender) {
				int listIndex = ontology_Classes.getSelectedIndex();
				String uri = ontology.get(ontologies.getSelectedIndex()).getBaseURI();
				String item = uri + ontology_Classes.getItemText(listIndex);
				addObject(item);
			}

		});

		/*
		 * File submit handling
		 */
		form.addFormHandler(new FormHandler() {

			@Override
			public void onSubmit(FormSubmitEvent event) {
				// logger.log(Level.SEVERE, "form title: "+
				// fileUpload.getFilename().substring(fileUpload.getFilename().lastIndexOf('\\')
				// + 1));
				ontName = fileUpload.getFilename().substring(fileUpload.getFilename().lastIndexOf('\\') + 1);

			}

			@Override
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				// TODO Auto-generated method stub

			}

		});
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				logger.log(Level.SEVERE, "form title: " + form.getTitle());
				form.reset();
				AsyncCallback<String> pathfile = new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						Window.alert("Fail");
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						path_of_uploaded_file = result;
						ontology.get(ontology.size() - 1).setFilePath(result);
						Window.alert("Pass");
						greetingService.greetServer(list, path_of_uploaded_file, 1, ontology_class);
						greetingService.greetServer(list, path_of_uploaded_file, 2, property_resource);
						greetingService.greetServer(list, path_of_uploaded_file, 3, property_literal);

						ontologies.addItem(ontology.get(ontology.size() - 1).getName());

						// logger.log(Level.SEVERE, baseURI);
					}

				};
				greetingService.filePath(pathfile);
				greetingService.getBaseURI(geturi);
				// greetingService.getOntName(getOntName);
				ontology.add(new Ontology(ontName, path_of_uploaded_file, baseURI, classes, properties, literals));
				// greetingService.getOntName(getOntName);
				if (ontology.size() == 1) {
					// populate_listBoxes();
				}
				logger.log(Level.SEVERE, "baseuri = " + baseURI);
			}

		});

	}

	protected void printSuggestedSubject(String content) {

		subject = webBox.getText().concat("/" + content.replace(' ', '_'));
		row = tripleTable.getRowCount();
		// logger.log(Level.SEVERE, "rowcount:" + row);
		content.replace(' ', '_');
		if ((tripleTable.getText(row - 1, 2).isEmpty())) {
			row--;
		}

		if (radioA.isChecked() || !radioB.isChecked()) {
			tripleTable.setText(row, 0, subject);
		} else {
			tripleTable.setText(row, 2, subject);
		}

		tripleTable.setWidget(0, 5, save);
		Button removeButton = new Button("x"); // Will remove a triple from the
												// list
		Button suggestion = new Button("Suggestions");
		tripleTable.setWidget(row, 3, removeButton);

		tripleTable.setWidget(row, 4, suggestion);
		removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				tripleTable.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent click) {
						// TODO Auto-generated method stub
						com.google.gwt.user.client.ui.HTMLTable.Cell cell = tripleTable.getCellForEvent(event);
						int cellIndex = cell.getCellIndex();
						int rowIndex = cell.getRowIndex();
						logger.log(Level.SEVERE, "cell:" + cellIndex);
						if (cellIndex == 3) {
							tripleTable.removeRow(rowIndex);
						}
						rowIndex = tripleTable.getRowCount();

						tripleTable.setWidget(rowIndex - 1, 5, save);
					}

				});
			}

		});
		
	}

	protected void populate_ClassBox(int index) {
		ontology_Classes.clear();

		ontology_Classes.addItem("NONE");
		// menuBar_3.addItem(text, cmd)
		// String variable = "";
		Iterator<String> it = ontology.get(index).getClasses().iterator();

		while (it.hasNext()) {
			final String variable = it.next();
			ontology_Classes.addItem(variable);
			adding_objects.addItem(variable, new Command() {

				@Override
				public void execute() {
					String p = null;
					if (tripleTable.getText(row - 1, 2).isEmpty())
						row--;

					if (tripleTable.getText(row, 1).isEmpty()) {
						tripleTable.setText(row, 2, variable);
					} else {
						p = tripleTable.getText(row, 1);
						p = p.substring(p.indexOf('#') + 1, p.length());
						logger.log(Level.SEVERE, p);
						if (ontology.get(ontologies.getSelectedIndex()).getProperties().contains(p) || p.equals("RDF.type")) {
							tripleTable.setText(row, 2, variable);
							logger.log(Level.SEVERE,"Object property selected");
						} else {
							if (variable.startsWith("http://")) {
								Window.alert("Must enter a literal value");
							} else {
								tripleTable.setText(row, 2, variable);
							}
						}
					}
				}

			});
		}

	}		

	protected void populate_PropertyBox(int index) {
		Iterator<String> it = ontology.get(index).getProperties().iterator();
//		property_Resources.addItem("RDF.type");
		adding_object_prop.addItem("RDF.type",  new Command(){
			@Override
			public void execute(){
				String o = null;
				if (tripleTable.getText(row, 2).length() > 0) {
					o = tripleTable.getText(row, 2);
					o = o.substring(o.indexOf('#') + 1, o.length());

					logger.log(Level.SEVERE, "This should be a resource predicate: "
							+ ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o));
					if (ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o)) {
						if ("RDF.type".endsWith("*")) {
							Window.alert("Must select Object-Property. Object is a resource");
						} else {
							tripleTable.setText(row, 1, "RDF.type");
						}
					} else {
						if ("RDF.type".endsWith("*")) {
							tripleTable.setText(row, 1, "RDF.type");
						} else {
							Window.alert("Must select Literal-Property");
						}
					}
				} else {
					tripleTable.setText(row, 1, "RDF.type");
				}
			}
		});
		while (it.hasNext()) {
			final String variable = it.next();
//			property_Resources.addItem(it.next());
			adding_object_prop.addItem(variable, new Command(){
				@Override
				public void execute(){
					String o = null;
					if (tripleTable.getText(row, 2).length() > 0) {
						o = tripleTable.getText(row, 2);
						o = o.substring(o.indexOf('#') + 1, o.length());

						logger.log(Level.SEVERE, "This should be a resource predicate: "
								+ ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o));
						if (ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o)) {
							if (variable.endsWith("*")) {
								Window.alert("Must select Object-Property. Object is a resource");
							} else {
								tripleTable.setText(row, 1, variable);
							}
						} else {
							if (variable.endsWith("*")) {
								tripleTable.setText(row, 1, variable);
							} else {
								Window.alert("Must select Literal-Property");
							}
						}
					} else {
						tripleTable.setText(row, 1, variable);
					}
				}
			});
		}
	}

	protected void populate_LiteralBox(int index) {
		Iterator<String> it = ontology.get(index).getLiterals().iterator();
		while (it.hasNext()) {
			final String variable = it.next();
//			property_Literals.addItem(it.next());
			adding_data_prop.addItem(variable, new Command(){
				@Override
				public void execute(){
					String o = null;
					if (tripleTable.getText(row, 2).length() > 0) {
						o = tripleTable.getText(row, 2);
						o = o.substring(o.indexOf('#') + 1, o.length());

						logger.log(Level.SEVERE, "This should be a resource predicate: "
								+ ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o));
						if (ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o)) {
							if (variable.endsWith("*")) {
								Window.alert("Must select Object-Property. Object is a resource");
							} else {
								tripleTable.setText(row, 1, variable);
							}
						} else {
							if (variable.endsWith("*")) {
								tripleTable.setText(row, 1, variable);
							} else {
								Window.alert("Must select Literal-Property");
							}
						}
					} else {
						tripleTable.setText(row, 1, variable);
					}
				}
			});
		}
	}

	protected void print_Ontology(String ontClass, ListBox box) {
		// TODO Auto-generated method stub
		box.addItem(ontClass);
	}

	@SuppressWarnings("deprecation")
	protected void printSubject() {

		subject = webBox.getText().concat("/" + content.getText().replace(' ', '_'));

		logger.log(Level.SEVERE, "rowcount:" + row);
		content.getText().replace(' ', '_');
		if (radioA.isChecked() || !radioB.isChecked()) {
			row = tripleTable.getRowCount();
			if ((tripleTable.getText(row - 1, 2).isEmpty() || (!tripleTable.getText(row - 1, 2).isEmpty() && tripleTable
					.getText(row - 1, 0).isEmpty()))) {
				row--;
			} else if (tripleTable.getText(row - 1, 0).isEmpty())
				row--;
			tripleTable.setText(row, 0, subject);

		} else {
			// Adding literal object
			String obj = content.getText().replace(' ', '_');
			if (!tripleTable.getText(row, 1).isEmpty()) {
				if (tripleTable.getText(row, 1).endsWith("*"))
					tripleTable.setText(row, 2, obj);
				else {
					Window.alert("Must select from Ontology Classes, predicate is an Object Property");
				}
			} else {
				tripleTable.setText(row, 2, obj);
			}
			return;
		}

		tripleTable.setWidget(0, 5, save);
		Button removeButton = new Button("x"); // Will remove a triple from the
												// list
		Button suggestion = new Button("Suggestions");
		tripleTable.setWidget(row, 3, removeButton);

		tripleTable.setWidget(row, 4, suggestion);
		removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				tripleTable.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent click) {
						com.google.gwt.user.client.ui.HTMLTable.Cell cell = tripleTable.getCellForEvent(event);
						int cellIndex = cell.getCellIndex();
						int rowIndex = cell.getRowIndex();
						logger.log(Level.SEVERE, "cell:" + cellIndex);
						if (cellIndex == 3) {
							tripleTable.removeRow(rowIndex);
						}
					}

				});
			}

		});

	}

	protected void addLitObject(String item) {
		tripleTable.setText(row, 2, item);
	}

	/*
	 * Adding Class Object to triple table
	 */
	protected void addObject(String item) {
		String p = null;
		if (tripleTable.getText(row - 1, 2).isEmpty())
			row--;

		if (tripleTable.getText(row, 1).isEmpty()) {
			tripleTable.setText(row, 2, item);
		} else {
			p = tripleTable.getText(row, 1);
			p = p.substring(p.indexOf('#') + 1, p.length());
			if (ontology.get(ontologies.getSelectedIndex()).getProperties().contains(p) || p.equals("RDF.type")) {
				tripleTable.setText(row, 2, item);
			} else {
				if (item.startsWith("http://")) {
					Window.alert("Must enter a literal value");
				} else {
					tripleTable.setText(row, 2, item);
				}
			}
		}
	}

	/*
	 * Adding either a O.Prop or D.Prop to prediate in triple table
	 */
	protected void addPredicate(String item) {
		String o = null;
		if (tripleTable.getText(row, 2).length() > 0) {
			o = tripleTable.getText(row, 2);
			o = o.substring(o.indexOf('#') + 1, o.length());

			logger.log(Level.SEVERE, "This should be a resource predicate: "
					+ ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o));
			if (ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o)) {
				if (item.endsWith("*")) {
					Window.alert("Must select Object-Property. Object is a resource");
				} else {
					tripleTable.setText(row, 1, item);
				}
			} else {
				if (item.endsWith("*")) {
					tripleTable.setText(row, 1, item);
				} else {
					Window.alert("Must select Literal-Property");
				}
			}
		} else {
			tripleTable.setText(row, 1, item);
		}
	}

	/*
	 * Get an arrayList of triple java objects to be sent to server side which
	 * in turn to be sent to triple store -> server
	 */
	protected String[] getTriples() {
		String[] contents = new String[3];
		String message = "";
		int rowcount = tripleTable.getRowCount();
		logger.log(Level.SEVERE, tripleTable.getText(0, 0));
		while (rowcount > 1) {
			logger.log(Level.SEVERE, "Rowcount is: " + rowcount);

			contents[0] = (tripleTable.getText(rowcount - 1, 0));
			contents[1] = (tripleTable.getText(rowcount - 1, 1));
			contents[2] = (tripleTable.getText(rowcount - 1, 2));
			message += "\nSubject: " + contents[0] + "\nPredicate: " + contents[1] + "\nObject: " + contents[2];
			logger.log(Level.SEVERE, contents[1]);
			tripleTable.removeRow(rowcount - 1);

			final AsyncCallback<String[]> sendToTripleStore = new AsyncCallback<String[]>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert("FAILED TO UPLOAD");
				}

				@Override
				public void onSuccess(String[] result) {
					// TODO Auto-generated method stub
					Window.alert("UPLOADED");
				}

			};
			greetingService.sendToTripleStore(contents, sendToTripleStore);
			logger.log(Level.SEVERE, "Uploaded a triple");
			rowcount = tripleTable.getRowCount();
			// logger.log(Level.SEVERE, "rowcount= " + rowcount);
		}

		// StringEscapeUtils seu = new StringEscapeUtils();
		HTML triples_sent = new HTML(message);
		dialogBoxContents.add(triples_sent);
		dialogBoxContents.add(close);
		dBox.setWidget(dialogBoxContents);
		dBox.center();
		// return new_triple_list;
		return contents;
	}

	private void populateSuggestedTriples(List<String[]> action) {
		logger.log(Level.SEVERE, "Size of sugT" + action.size());
		Iterator<String[]> it = action.iterator();
		while (it.hasNext()) {
			String temp[] = new String[3];
			temp = it.next();
			logger.log(Level.SEVERE, temp[0] + " " + temp[1] + " " + temp[2]);

			ft.setText(0, 0, "Subject");
			ft.setText(0, 1, "Predicate");
			ft.setText(0, 2, "Object");
			ft.setText(0, 3, "Add");
			int rcount = ft.getRowCount();
			ft.setText(rcount, 0, temp[0]);
			ft.setText(rcount, 1, temp[1]);
			ft.setText(rcount, 2, temp[2]);

			cb = new CheckBox("Add");
			// cb.setChecked(true);
			cb.setValue(false);
			ft.setWidget(rcount, 3, cb);

			cb.addClickHandler(new ClickHandler() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(ClickEvent event) {
					boolean checked = ((CheckBox) event.getSource()).isChecked();
				}

			});
			ft.addClickHandler(new ClickHandler() {
				int count = 0;

				@Override
				public void onClick(ClickEvent event) {
					// int count = 0;
					if (count < 1) {
						com.google.gwt.user.client.ui.HTMLTable.Cell cell = ft.getCellForEvent(event);
						int cellIndex = cell.getCellIndex();
						int rowIndex = cell.getRowIndex();
						logger.log(Level.SEVERE, "cell:" + cellIndex + "~ Row:" + (rowIndex));
						if (cellIndex == 3 && cb.getValue()) {
						}
					}
					count++;
				}

			});
		}
		Label lb = new Label();
		popupContents.add(popupHolder);
		popupContents.add(ft);
		lb.setText("* defines a Literal value");
		popupContents.add(lb);
		popup.center();

	}

	@SuppressWarnings("deprecation")
	protected void loadPageTwo(String path) {
		RootPanel.get("stockList").clear();
		// mainPanel.clear();
		// mainPanel.add(home_page);
		logger.log(Level.SEVERE, ontologies.getSelectedIndex() + " and " + path);

		/* second page */
		final String export_path = path;
		tree_grid.add(browseTree);
		instance_link.add(instance_grid);
		instance_link.addStyleName("treeAndGrid");
		instance_grid.addStyleName("treeAndGrid");

		instance_link.add(link);
		instance_link.add(to_content);

		queryPanel.add(ontologies);
		queryPanel.add(ontology_Classes);
		queryPanel.add(subjectQuery);

		tree_grid.add(instance_link);
		tree_grid.add(queryPanel);
		page2Panel.add(entercontext);
		page2Panel.add(tree_grid);
		page2Panel.addStyleName("treeAndGrid");
		ClickHandler link_to_page = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				logger.log(Level.SEVERE, "URL: " + link_to_content_page);
				Window.open(link_to_content_page, "Content Page", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes");
			}

		};
		ClickHandler getWebsite = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				com.google.gwt.user.client.ui.HTMLTable.Cell cell = instance_grid.getCellForEvent(event);

				instance_grid.getRowFormatter().removeStyleName(rowIndex, "selectCell");
				int cellIndex = cell.getCellIndex();
				rowIndex = cell.getRowIndex();
				instance_grid.removeStyleName("selectCell");
				if (cellIndex == 0) {
					instance_grid.getRowFormatter().addStyleName(rowIndex, "selectCell");
					// instance_grid.getColumnFormatter().addStyleName(cellIndex,
					// "selectCell");
					link_to_content_page = instance_grid.getText(rowIndex, 0);
					link_to_content_page = link_to_content_page.substring(0, link_to_content_page.lastIndexOf('/'));
					logger.log(Level.SEVERE, "URL: " + link_to_content_page);
				}

			}
		};
		ClickHandler page2_queryHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (subjectQuery.getText().equals("")) {

				}
				instance_grid.removeAllRows();
//				greetingService.getQueryInstances(
//						subjectQuery.getText().equals("") ? "NONE" : webBox.getText()
//								.concat("/" + subjectQuery.getText().replace(' ', '_')),
//						property_Resources.getItemText(property_Resources.getSelectedIndex()).equals("NONE") ? "NONE" : ontology
//								.get(ontologies.getSelectedIndex()).getBaseURI()
//								.concat(property_Resources.getItemText(property_Resources.getSelectedIndex())),
//
//						ontology_Classes.getItemText(ontology_Classes.getSelectedIndex()).equals("NONE") ? "NONE" : ontology
//								.get(ontologies.getSelectedIndex()).getBaseURI()
//								.concat(ontology_Classes.getItemText(ontology_Classes.getSelectedIndex())),
//						entercontext.getText().equals("") ? "NONE" : entercontext.getText(), new queryInstances());
			}

		};
		to_content.addClickHandler(link_to_page);
		instance_grid.addClickHandler(getWebsite);
		buildTree(export_path);
		browseTree.addStyleName("treeAndGrid");

		greetingService.getChildren(export_path, "Thing", new TreeRootCallback(browseTree));
		// Gets instances for selected tree item!
		browseTree.addTreeListener(new TreeListener() {

			@Override
			public void onTreeItemSelected(TreeItem item) {
				logger.log(Level.SEVERE, "Item = " + item.getText());
				instance_grid.removeAllRows();
				greetingService.getInstances(export_path, item.getText(), entercontext.getText(), new TreeItemInstances());
			}

			@Override
			public void onTreeItemStateChanged(TreeItem item) {

			}

		});

		instance_grid.setText(0, 0, "Row 1:Col 1");

		RootPanel.get("newList").add(home_page);
		logger.log(Level.SEVERE, "Cleared");
		RootPanel.get("newList").add(page2Panel);
		int left2, top2;
		left2 = Window.getClientWidth() / 5;
		top2 = Window.getClientHeight() / 5;
		// RootPanel.get("newList").add(queryPanel, left2, top2);
	}

	private void buildTree(String path) {
		TreeItem root = new TreeItem(LOADING_ITEMS);
		browseTree.addItem(root);
		final String ont = path;
		browseTree.addOpenHandler(new OpenHandler<TreeItem>() {

			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				if (needsLoading(event.getTarget())) {

					greetingService.getChildren(ont, event.getTarget().getText(), new TreeItemCallback(event.getTarget()));
				}
			}
		});
		logger.log(Level.SEVERE, "Tree Built");
	}

	private boolean needsLoading(TreeItem item) {
		return item.getChildCount() == 1 && LOADING_ITEMS.equals(item.getChild(0).getText());
	}

	public final class queryInstances implements AsyncCallback<ArrayList<String[]>> {
		public void onFailure(Throwable caught) {

		}

		public void onSuccess(ArrayList<String[]> result) {
			Window.alert("GOT INSTANCES BACK");
			instance_grid.removeAllRows();
			int grid_row = 0;
			Iterator<String[]> ii = result.iterator();
			while (ii.hasNext()) {
				String[] temp = ii.next();
				for (int i = 0; i < temp.length; i++) {
					instance_grid.setText(grid_row, i, temp[i]);
				}
				grid_row++;
			}
		}
	}

	public final class downloadRepository implements AsyncCallback<String> {

		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			Window.alert("fail on download repository");
		}

		public void onSuccess(String names) {
			Window.alert(names);
			export_fp = names;
			ontology.add(new Ontology("Export", export_fp, null, null, null, null));
			// ontologies.addItem(ontology.get(ontology.size() - 1).getName());
			// ontologies.setSelectedIndex(ontology.size() - 1);
			if (repository_downloaded)
				loadPageTwo(export_fp);
		}
	}

	public static final class TreeRootCallback implements AsyncCallback<ArrayList<String>> {

		private Tree browseTree;

		public TreeRootCallback(Tree browseTree) {
			super();
			this.browseTree = browseTree;
		}

		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			Window.alert("Fail on tree root callback");
		}

		public void onSuccess(ArrayList<String> names) {
			browseTree.removeItems();
			for (String name : names) {
				logger.log(Level.SEVERE, "Gotten first rot of tree");
				TreeItem ti = new TreeItem(name);
				ti.addItem(LOADING_ITEMS);
				browseTree.addItem(ti);
			}
		}

	}

	public static final class TreeItemCallback implements AsyncCallback<ArrayList<String>> {

		private TreeItem treeItem;

		public TreeItemCallback(TreeItem treeItem) {
			super();
			this.treeItem = treeItem;
		}

		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			Window.alert("fail on tree item callback");
		}

		public void onSuccess(ArrayList<String> names) {
			treeItem.removeItems();
			for (String name : names) {
				TreeItem ti = new TreeItem(name);
				ti.addItem(LOADING_ITEMS);
				treeItem.addItem(ti);
			}
		}
	}

	public final class TreeItemInstances implements AsyncCallback<ArrayList<String[]>> {

		@Override
		public void onFailure(Throwable caught) {

		}

		@Override
		public void onSuccess(ArrayList<String[]> result) {
			logger.log(Level.SEVERE, "Instances size: " + result.size());
			Window.alert("GOT INSTANCES BACK");
			instance_grid.clear();
			int grid_row = 0;
			Iterator<String[]> ii = result.iterator();
			while (ii.hasNext()) {
				String[] temp = ii.next();
				for (int i = 0; i < temp.length; i++) {
					instance_grid.setText(grid_row, i, temp[i]);
				}
				grid_row++;
			}
		}

	}

	protected void loadHomePage() {
		RootPanel.get("newList").clear();

		queryPanel.clear();
		instance_link.clear();
		page2Panel.clear();
		uploadedOntologies.add(ontologies);



		RootPanel.get("stockList").add(frame, RootPanel.get("stockList").getAbsoluteLeft(), RootPanel.get("stockList").getAbsoluteTop());
		RootPanel.get("stockList").add(bottomOfScreen, RootPanel.get("stockList").getAbsoluteLeft(), Window.getClientHeight());
		// RootPanel.get().add(tripleTable, 5,
		// (int) ((Window.getClientHeight() / 1.1) + 30));
		// RootPanel.get("stockList").add(save, 700, (int)
		// ((Window.getClientHeight() / 1.1) + 30));
		RootPanel.get("stockList").add(form, frame.getOffsetWidth() + 10, frame.getAbsoluteTop());
		RootPanel.get("stockList").add(mainPanel, frame.getOffsetWidth() + 10, form.getOffsetHeight() + frame.getAbsoluteTop());

		browseTree.clear();

	}
}