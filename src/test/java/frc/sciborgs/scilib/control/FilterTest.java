package frc.sciborgs.scilib.control;

import java.lang.System.Logger;

class FilterTest {
  private static final Logger LOGGER = System.getLogger(FilterTest.class.getName());

  // TODO hal cannot be initialized
  // @Test
  // void derivativeAndIntegralAreInversesTest() {
  //     assert HAL.initialize(500, 0);
  //     // Create D and I filters
  //     Filter D = Filter.DT(0);
  //     Filter I = Filter.IT(0);

  //     // Create positions
  //     List<Double> positions = DoubleStream.iterate(0.0, d -> d < 20.0, d -> d + 1.0).boxed()
  //             .collect(Collectors.toList());

  //     // D filter
  //     List<Double> velocities = positions.stream().map(x ->
  // D.calculate(x)).collect(Collectors.toList());

  //     // I filter
  //     List<Double> integrated = velocities.stream().map(x ->
  // I.calculate(x)).collect(Collectors.toList());

  //     for (int i = 0; i < positions.size(); i++) {
  //         assertEquals(integrated.get(i), positions.get(i));
  //     }
  // }
}
