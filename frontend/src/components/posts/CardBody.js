import React, { useState } from 'react'
import Carousel from '../Carousel'

function CardBody({post}) {
    const [readMore, setReadMore] = useState(false)
    return (
        <div className="cardbody">
            <div className="cardbody_image">
            {
                post.postImages.length>0 &&
                <Carousel postImages={post.postImages}></Carousel>
            }
            </div>
            <div className="cardbody_content">
                <span>
                {
                    post.content.length<60
                    ? post.content
                    : readMore ? post.content + '' : post.content.slice(0,60)+ "..."
                }
                </span>
                {
                    post.content.length> 60 &&
                    <span className="read_more" onClick={()=>setReadMore(!readMore)}>
                        {readMore ? 'Hide Content' : 'Read More'}
                    </span>
                }
            </div>
        </div>
    )
}

export default CardBody
