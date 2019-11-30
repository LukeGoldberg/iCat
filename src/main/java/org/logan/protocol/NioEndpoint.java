/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.logan.protocol;

import java.net.InetAddress;
import java.util.logging.Logger;

import org.logan.core.container.Engine;
import org.logan.util.CatProperties;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LoggingHandler;

/**
 * NIO tailored thread pool, providing the following services:
 * <ul>
 * <li>Socket acceptor thread</li>
 * <li>Socket poller thread</li>
 * <li>Worker threads pool</li>
 * </ul>
 *
 * When switching to Java 5, there's an opportunity to use the virtual
 * machine's thread pool.
 *
 * @author Mladen Turk
 * @author Remy Maucherat
 */
public class NioEndpoint {
	
	private static final Logger logger = Logger.getLogger("NioEndpoint");
	
	private Http11NioProtocol protocolHandler;
	
	public NioEndpoint(Http11NioProtocol protocolHandler) {
		this.protocolHandler = protocolHandler;
	}
	
	// equals to 8080 @ Tomcat
	private static final int PORT = 8007;
	
	EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	
	EventLoopGroup workGroup = new NioEventLoopGroup();
	
    private static final Logger log = Logger.getLogger("NioEndpoint");
    
    private InetAddress address;
    
    private int acceptCount = 100;
    
    private boolean running = false;

    public void init() {
        logger.info("NioEndpoint init starting...");
        // do nothing
    }
    
    public void start() {
    	try {
    		ServerBootstrap b = new ServerBootstrap();
    		b.group(bossGroup, workGroup)
    		.channel(NioServerSocketChannel.class)
    		.option(ChannelOption.SO_BACKLOG, 100)
    		.handler(new LoggingHandler())
    		.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new HttpServerCodec());
			        p.addLast(new HttpServerExpectContinueHandler());
			        Engine engine = protocolHandler.getConnector()
		                                                      .getService()
		                                                      .getEngine();
					p.addLast(new CatServerHandler(engine));
				}
    		});
    		// Start the server.
//            ChannelFuture f = b.bind(PORT).sync();
    		
    		Integer port = PORT;
    		try {
    			port = Integer.parseInt(CatProperties.getProperty("server.port"));    			
    		} catch(NumberFormatException ignore) {
    			
    		}
            ChannelFuture f = b.bind(port).sync();
            running = true;
            logger.info("http waiting for message on " + port);
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    
    public void closeServerSocketGracefully() throws Exception {
// just shutdown netty gracefully    
    	bossGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
    }
    
    public int getAcceptCount() { 
    	return acceptCount; 
    }
    
    public InetAddress getAddress() {
    	return address;
    }
    
}
