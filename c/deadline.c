/*
	Example code to explore the deadline device.

	Author: Martin Schoeberl
	Copyright: DTU, BSD License
*/

#include <machine/patmos.h>
#include <stdio.h>

int main() {

  // Pointer to the deadline device
  volatile _IODEV int *dead_ptr = (volatile _IODEV int *) 0xf00b0000;
  // Measure execution time with the clock cycle timer
  volatile _IODEV int *timer_ptr = (volatile _IODEV int *) 0xf0020004;
  // use function
  int start, end;
  int val;

  start = *timer_ptr;
  end = *timer_ptr;
  printf("Empty measurement %d\n", end-start);


/*
  printf("Down counter value %d\n", *dead_ptr);
  *dead_ptr = 1000;
  start = *dead_ptr;
  printf("Down counter value after set %d\n", *dead_ptr);
  printf("Down counter value after set %d\n", *dead_ptr);
*/

  // Use the counter and the timer for measurement

  // Show relative example

  start = *timer_ptr;
  *dead_ptr = 500;
  val = *dead_ptr; // this read is delayed by 500 clock cycles
  end = *timer_ptr;
  printf("Delay measurement %d %d\n", end-start, val);

  // Show how a check after the time does not delay (measure with timer)

  start = *timer_ptr+600;
  *dead_ptr = 500;
  while (*timer_ptr - start < 0) {
    ;
  }
  start = *timer_ptr;
  val = *dead_ptr; // this read is delayed by 500 clock cycles
  end = *timer_ptr;
  printf("Delay measurement after expired %d %d\n", end-start, val);

  // Show absolute value in a simple example

  // Show how periodic work can be done in perfection with absolute values



}
