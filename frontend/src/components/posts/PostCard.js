import  CardHeader  from './CardHeader'
import React from 'react'
import CardBody from './CardBody'
import CardFooter from './CardFooter'

function PostCard({post}) {
    return (
        <div className="postcard">
            <CardHeader post={post}/>
            <hr/>
            <CardBody post={post}/>
            <CardFooter post={post} />
        </div>
    )
}

export default PostCard
