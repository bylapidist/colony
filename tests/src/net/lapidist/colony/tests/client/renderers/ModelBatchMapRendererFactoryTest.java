package net.lapidist.colony.tests.client.renderers;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import net.lapidist.colony.client.renderers.MapRenderer;
import net.lapidist.colony.client.renderers.ModelBatchMapRendererFactory;
import net.lapidist.colony.tests.GdxTestRunner;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.mockConstruction;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(GdxTestRunner.class)
public class ModelBatchMapRendererFactoryTest {

    @Test
    public void createsRendererWithResources() {
        World world = new World(new WorldConfigurationBuilder().build());
        try (MockedConstruction<ModelBatch> ignored = mockConstruction(ModelBatch.class)) {
            ModelBatchMapRendererFactory factory = new ModelBatchMapRendererFactory();
            MapRenderer renderer = factory.create(world);
            assertNotNull(renderer);
        }
        world.dispose();
    }
}
