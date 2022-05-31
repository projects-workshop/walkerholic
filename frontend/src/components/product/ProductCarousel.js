import React from 'react'
import { useState } from 'react'
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css";



function ProductCarousel({images}) {

    return (
        <div className="carousel-wrapper">
        <Carousel  showArrows={false} infiniteLoop={true} showIndicators={false} showStatus={false}>
            {
                images.map((image, index)=>(
                    image.imageUrl.match(/video/i)||image.imageUrl.match(/mp4/i)||image.imageUrl.match(/avi/i)||image.imageUrl.match(/mov/i)||image.imageUrl.match(/wmv/i)
                    ? <video key={index} src={image.imageUrl} controls className="d-block w-100" alt={image.data}></video>
                    : <img key={index} src={image.imageUrl} className="d-block w-100" alt={image.data} />
                ))
            }
        </Carousel>
    </div>

    )
}

export default ProductCarousel
