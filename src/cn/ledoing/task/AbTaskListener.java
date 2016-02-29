/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ledoing.task;


// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbTaskListener.java 
 * 描述：任务执行的控制父类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-9-2 下午12:52:13
 */
public class AbTaskListener {
    
    /**
     * Gets the.
     *
     * @return 返回的结果对象
     */
    public void get(){};
    
    /**
     * 描述：执行开始后调用.
     * */
    public void update(){}; 
    
	/**
	 * 监听进度变化.
	 *
	 * @param values the values
	 */
	public void onProgressUpdate(Integer... values){};

}
