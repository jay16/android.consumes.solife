package us.solife.androidcharts.event;

/*
 * ITouchEventNotify.java
 * Android-Charts
 *
 * Created by limc on 2011/05/29.
 *
 * Copyright 2011 limc.cn All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import us.solife.androidcharts.view.GridChart;

/**
 * 
 * <p>Interface for chart which is support send notify after touch event happened</p>
 * <p>���å����٥�Ȥ�֪ͨ���ܤΥ��֥������ȤΥ��󥿥ե��`��</p>
 * <p>touch�¼�������֧�ֶ��ⷢ���¼���Ϣ�Ĵ������ӿ�</p>
 *
 * @author limc 
 * @version v1.0 2013/05/30 17:57:32 
 * @see ITouchEventResponse
 */
public interface ITouchEventNotify {

	/**
	 * <p>Notify all ITouchEventResponse objects</p>
	 * <p>ȫ��ITouchEventResponse�쥹�ݥ󥹥��֥������Ȥ�֪ͨ</p>
	 * <p>֪ͨȫ��ITouchEventResponse��Ӧ����</p>
	 * @param chart
	 * <p>source chart</p>
	 * <p>���`������`��</p>
	 * <p>Դͷ����</p>
	 */
	public void notifyEventAll(GridChart chart);
	
	/**
	 * <p>Add a ITouchEventResponse object by its index</p>
	 * <p>ITouchEventResponse�쥹�ݥ󥹥��֥������Ȥ�׷��</p>
	 * <p>����ITouchEventResponse��Ӧ����</p>
	 * @param notify
	 * <p>ITouchEventResponse object</p>
	 * <p>ITouchEventResponse ���֥�������</p>
	 * <p>����</p>
	 */
	public void addNotify(ITouchEventResponse notify);
	
	/**
	 * <p>Remove a ITouchEventResponse object by its index</p>
	 * <p>ITouchEventResponse�쥹�ݥ󥹥��֥������Ȥ�����</p>
	 * <p>ɾ��ITouchEventResponse��Ӧ����</p>
	 * @param i
	 * <p>index</p>
	 * <p>����ǥå���</p>
	 * <p>index</p>
	 */
	public void removeNotify(int i);
	
	/**
	 * <p>Remove all ITouchEventResponse objects</p>
	 * <p>ȫ��ITouchEventResponse�쥹�ݥ󥹥��֥������Ȥ�����</p>
	 * <p>ɾ��ȫ��ITouchEventResponse��Ӧ����</p>
	 */
	public void removeAllNotify();
}
