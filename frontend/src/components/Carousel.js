import React from 'react'

function Carousel({postImages}) {

    const isActive = (index) =>{
        if(index === 0) return "active"
    }
    return (
        <div>
            <div id="carouselExampleCaptions" class="carousel slide" data-ride="carousel">
                <ol class="carousel-indicators">
                    {
                        postImages.length >1 &&postImages.map((image, index)=>(
                            <li data-target="#carouselExampleCaptions" data-slide-to={index} className={isActive(index)} key={index}></li>
                        ))
                    }
                </ol>
                <div class="carousel-inner">
                    {
                        postImages.map((image, index)=>(
                            <div className={`carousel-item ${isActive(index)}`} key={index}>
                                {
                                    image.imageUrl.match(/video/i)||image.imageUrl.match(/mp4/i)||image.imageUrl.match(/avi/i)||image.imageUrl.match(/mov/i)||image.imageUrl.match(/wmv/i)
                                    ? <video src={image.imageUrl} controls className="d-block w-100" alt={image.data}></video>
                                    : <img src={image.imageUrl} className="d-block w-100" alt={image.data} />
                            }
                            </div>
                        ))
                    }
                </div>
                {
                    postImages.length >1 &&
                    <>
                        <a className="carousel-control-prev" href="carouselExampleCaptions" role="button" data-slide="prev"
                        style={{width: '5%'}}>
                            <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span className="sr-only">Previous</span>
                        </a>

                        <a className="carousel-control-next" href="carouselExampleCaptions" role="button" data-slide="next"
                        style={{width: '5%'}}>
                            <span className="carousel-control-next-icon" aria-hidden="true"></span>
                            <span className="sr-only">Next</span>
                        </a>
                    </>
                }
            </div>
        </div>
    )
}

export default Carousel
