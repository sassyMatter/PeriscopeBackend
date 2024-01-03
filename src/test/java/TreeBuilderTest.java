import com.app.models.canvas.CanvasData;
import com.app.models.canvas.CanvasObject;
import com.app.models.canvas.Connection;
import com.app.models.canvasSchema.TreeNode;
import com.app.services.core.TreeBuilderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
public class TreeBuilderTest {

    @Test
    public void testBuildTree() {
        // Create sample CanvasData objects


        CanvasData canvasData = createSampleCanvasData();

        log.info("canvas Data {} ", canvasData);

        TreeBuilderService treeBuilderService = new TreeBuilderService();

        TreeNode root = treeBuilderService.buildTree(canvasData);

        log.info("tree:: {}" , root);
        // Perform assertions on the tree structure
        Assertions.assertNotNull(root);
        Assertions.assertEquals("input", root.getData().getType());

        List<TreeNode> children = root.getConnections();
        Assertions.assertEquals(1, children.size());

        TreeNode child = children.get(0);
        log.info("Child :: {}, {}", child, children);
        Assertions.assertEquals("d08ac4b3-4306-4ae6-9568-cb09e45fb2f0", child.getData().getId());
        // Add more assertions for child properties or further nesting if needed
    }

    private CanvasData createSampleCanvasData() {
        // Create and return a sample CanvasData object
        CanvasObject inputObject = new CanvasObject();
        inputObject.setType("input");
        inputObject.setId("d9ccd235-5caa-4d86-9e03-e421a35bdc3b");

        CanvasObject connectedObject = new CanvasObject();
        connectedObject.setId("d08ac4b3-4306-4ae6-9568-cb09e45fb2f0");

        Connection connection = new Connection();
        connection.setId("d08ac4b3-4306-4ae6-9568-cb09e45fb2f0");
        List<Connection> connections = new ArrayList<>();
        connections.add(connection);

        inputObject.setConnections((ArrayList<Connection>) connections);

        CanvasData canvasData = new CanvasData();
        List<CanvasObject> canvasObjects = new ArrayList<>();
        canvasObjects.add(inputObject);
        canvasObjects.add(connectedObject);
        canvasData.setObjects((ArrayList<CanvasObject>) canvasObjects);

        return canvasData;
    }


}
