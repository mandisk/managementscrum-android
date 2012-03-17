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

package org.minftel.mscrum.chart.flot.data;

public class RectOffset {
	public int left = 0;
	public int top = 0;
	public int right = 0;
	public int bottom = 0;

	public void reset(int _left, int _top, int _right, int _bottom) {
		this.left = _left;
		this.top = _top;
		this.right = _right;
		this.bottom = _bottom;
	}
}
