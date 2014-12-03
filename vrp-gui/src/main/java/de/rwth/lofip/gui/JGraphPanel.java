package de.rwth.lofip.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;



import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import de.rwth.lofip.library.AbstractPointInSpace;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.util.CustomerInTour;

public class JGraphPanel extends mxGraphComponent {

    private static final long serialVersionUID = 1L;

    private mxGraph graph;

    private VrpProblem problem;
    private Solution solution;

    public JGraphPanel() {
        super(new mxGraph());
        setBackground(Color.WHITE);
        graph = getNewMxGraph();
        setGraph(graph);
        setDragEnabled(false);
        setConnectable(false);
        getViewport().setBackground(Color.WHITE);
        setToolTips(true);
    }

    private mxGraph getNewMxGraph() {
        graph = new mxGraph() {
            @Override
            public String getToolTipForCell(Object cell) {
                if (model.isVertex(cell)) {
                    Object value = ((mxCell) cell).getValue();
                    if (value instanceof Customer) {
                        Customer c = (Customer) value;
                        StringBuilder tooltipString = new StringBuilder(
                                "<html>");
                        tooltipString.append("Customer No.: ");
                        tooltipString.append(c.getCustomerNo())
                                .append("<br />");
                        tooltipString.append("Demand: ").append(c.getDemand())
                                .append("<br />");
                        tooltipString.append("Time Window: [")
                                .append(c.getTimeWindowOpen()).append(", ");
                        tooltipString.append(c.getTimeWindowClose())
                                .append("]");
                        return tooltipString.toString();
                    }
                    if (value instanceof CustomerInTour) {
                        CustomerInTour c = (CustomerInTour) value;
                        StringBuilder tooltipString = new StringBuilder(
                                "<html>");
                        tooltipString.append("Customer No.: ");
                        tooltipString.append(c.getCustomer().getCustomerNo())
                                .append("<br />");
                        tooltipString.append("Position " + c.getPosition())
                                .append("<br />");
                        tooltipString.append("Demand: ")
                                .append(c.getCustomer().getDemand())
                                .append("<br />");
                        tooltipString.append("Time Window: [")
                                .append(c.getCustomer().getTimeWindowOpen())
                                .append(", ");
                        tooltipString.append(
                                c.getCustomer().getTimeWindowClose()).append(
                                "]<br />");
                        tooltipString.append("Vehicle arrival: ")
                                .append(c.getArrivalTime()).append("<br />");
                        tooltipString.append("Leaving time: ")
                                .append(c.getEarliestLeavingTime())
                                .append("<br />");
                        tooltipString.append("Inserted in "
                                + c.getInsertedInIteration() + " by "
                                + c.getInsertionHeuristic());
                        return tooltipString.toString();
                    }
                }
                return super.getToolTipForCell(cell);
            }

            @Override
            protected mxGraphView createGraphView() {
                return new mxGraphView(this) {

                    /**
                     * Enhance updateLabel method to calculate the label width
                     * and if needed clip it and end it with "..."
                     */
                    @Override
                    public void updateLabel(mxCellState state) {

                        super.updateLabel(state);

                        // get label which got set by the super method
                        Object value = ((mxCell) state.getCell()).getValue();
                        Customer c = null;
                        if (value instanceof Customer) {
                            c = (Customer) value;
                        }
                        if (value instanceof CustomerInTour) {
                            c = ((CustomerInTour) value).getCustomer();
                        }
                        if (c != null) {
                            String newLabel = "Customer "
                                    + String.valueOf(c.getCustomerNo());
                            state.setLabel(newLabel);
                        }
                    }
                };
            }
        };

        graph.setCellsSelectable(false);
        graph.setCellsEditable(false);
        graph.getStylesheet().getDefaultEdgeStyle()
                .put(mxConstants.STYLE_NOLABEL, "1");
        return graph;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graph = getNewMxGraph();
        drawAllVerticesAndEdges(graph);
        graph.getView().revalidate();
        setGraph(graph);
        // zoom to show the complete graph - and only the complete graph
        // double newScale = 1;

        // Dimension graphSize = getGraphControl().getSize();
        // Dimension viewPortSize = getViewport().getSize();
        //
        // int gw = (int) graphSize.getWidth();
        // int gh = (int) graphSize.getHeight();
        //
        // if (gw > 0 && gh > 0) {
        // int w = (int) viewPortSize.getWidth();
        // int h = (int) viewPortSize.getHeight();
        //
        // newScale = Math.min((double) w / gw, (double) h / gh);
        // }
        //
        // zoom(newScale);
    }

    private void drawAllVerticesAndEdges(mxGraph graph) {
        if (problem == null) {
            return;
        }

        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        // get the maximum x and y value
        double maxX = Math.max(0, problem.getDepot().getxCoordinate());
        double maxY = Math.max(0, problem.getDepot().getyCoordinate());

        for (Customer c : problem.getCustomers()) {
            maxX = Math.max(maxX, c.getxCoordinate());
            maxY = Math.max(maxY, c.getyCoordinate());
        }

        try {
            // this map is needed to find the correct graph object for every
            // vertex
            Map<AbstractPointInSpace, Object> pointsToGraphObjects = new HashMap<AbstractPointInSpace, Object>(
                    problem.getCustomers().size() + 1);

            double rw = (getSize().getWidth() / maxX) * 0.8;
            double rh = (getSize().getHeight() / maxY) * 0.8;

            // insert the depot
            Object depot = graph.insertVertex(parent, null, "Depot", rw
                    * problem.getDepot().getxCoordinate(), rh
                    * problem.getDepot().getyCoordinate(), 10, 10,
                    "fillColor=red");
            pointsToGraphObjects.put(problem.getDepot(), depot);

            // now insert the vertices
            if (solution == null) {
                for (Customer c : problem.getCustomers()) {
                    Object vertex = graph.createVertex(parent, null, c,
                            rw * c.getxCoordinate(), rh * c.getyCoordinate(),
                            5, 5, null, false);
                    pointsToGraphObjects.put(c, graph.addCell(vertex));
                }
            } else {
                for (CustomerInTour c : solution.getCustomersInTours()) {
                    Object vertex = graph.createVertex(parent, null, c, rw
                            * c.getCustomer().getxCoordinate(), rh
                            * c.getCustomer().getyCoordinate(), 5, 5, null,
                            false);
                    pointsToGraphObjects.put(c.getCustomer(),
                            graph.addCell(vertex));
                }
                for (Edge e : solution.getEdges()) {
                    graph.insertEdge(parent, null, "",
                            pointsToGraphObjects.get(e.getPointOne()),
                            pointsToGraphObjects.get(e.getPointTwo()));
                }
            }
        } finally {
            graph.getModel().endUpdate();
        }
    }

    public VrpProblem getVrpProblem() {
        return problem;
    }

    public void setVrpProblem(VrpProblem problem) {
        this.problem = problem;
        drawAllVerticesAndEdges(getNewMxGraph());
        graph.getView().revalidate();
        setGraph(graph);
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        drawAllVerticesAndEdges(getNewMxGraph());
        graph.getView().revalidate();
        setGraph(graph);
    }
}
