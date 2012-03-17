/*
Copyright 2010 Kxu
Copyright 2010 TheChatrouletteGirls.Com.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.minftel.mscrum.chart.flot.options;

import org.minftel.mscrum.chart.flot.format.DoubleFormatter;
import org.minftel.mscrum.chart.flot.format.TickFormatter;

public class AxisOption {
	public String mode = null;
	public DoubleFormatter transform = null;
	public DoubleFormatter inverseTransform = null;
	public double min = Double.NaN;
	public double max = Double.NaN;
	public double autoscaleMargin = Double.NaN;

	public Object ticks = null;
	public TickFormatter tickFormatter = null;
	public int labelWidth = -1;
	public int labelHeight = -1;

	public int tickDecimals = -1;
	public double tickSize = Double.MIN_VALUE;
	public double minTickSize = Double.MIN_VALUE;
	public String[] monthNames = null;
	public String timeformat = null;
	public Boolean twelveHourClock = false;
}
