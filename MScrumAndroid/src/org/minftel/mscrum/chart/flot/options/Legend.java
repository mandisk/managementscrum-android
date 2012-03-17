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

import org.minftel.mscrum.chart.flot.format.StringFormatter;

public class Legend {

	public Boolean show = true;
	public int noColumns = 1;
	public StringFormatter labelFormatter = null;
	public int labelBoxBorderColor = 0x333333;
	public Object container = null;
	public String position = "ne";
	public int margin = 5;
	public Object backgroundColor = new Integer(0xffffff);
	public int labelColor = 0x000000;
}
