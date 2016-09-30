class GeoSearchDownloader:
    def __init__(self, kw_dir, cities_dir, countries_dir, dst_dir,
                 t_key, t_secret, ta_token, ta_secret, language):
        self._kw_dir = kw_dir
        self._cities_dir = cities_dir
        self._countries_dir = countries_dir
        self._dst_dir = dst_dir

        self.t_key = t_key 
        self.t_secret = t_secret 
        self.ta_token = ta_token 
        self.ta_secret = ta_secret 
        self.language = language


    def download(self, numTweets, verbose=False):
        from subprocess import call

        keywords_f = open(self._kw_dir)
        keywords = keywords_f.readlines()
        keywords = [kw[:-1] for kw in keywords]

        countries_f = open(self._countries_dir)
        countries = countries_f.readlines()
        countries = [country[:-1] for country in countries]
        countries_f.close()


        call(['mkdir', '-p', self._dst_dir])

        for country in countries:
            print('{}{}.txt'.format(self._cities_dir, country))
            
            cities_f = open('{}{}.txt'.format(self._cities_dir, country))
            cities = cities_f.readlines()
            cities = [city[:-1] for city in cities]
            cities_f.close()

            for keyword in keywords:
                for city in cities:
                    data = city.split()
                    name = data[0]
                    lat = data[1]
                    lon = data[2]
                    rad = data[3]

                    if verbose:
                        print('-------------------')
                        print('Starting geo search')
                        print('-------------------')

                        print('\tCountry: {}'.format(country))
                        print('\tCity: {}'.format(city))
                        print('\tKeyword: {}'.format(keyword))
                    
                    command = [    
                        'java', '-Xmx7000m', '-Xms2500m', '-jar', 
                        'GeoSearch/TwitterDownloader/dist/TwitterDownloader.jar',
                        '-nt', str(numTweets), 
                        '-lat', lat,
                        '-lon', lon,
                        '-rad', rad,
                        '-lm', 'download/profiles/',
                        '-t_key', self.t_key,
                        '-t_secret', self.t_secret,
                        '-ta_token', self.ta_token,
                        '-ta_secret', self.ta_secret,
                        '-l', self.language,
                        '-q', keyword
                    ]

                    
                    dst_dir = '{}{}_{}_{}_{}_{}.twt'.format(self._dst_dir,
                        country,
                        keyword,
                        lat,
                        lon,
                        name)

                    dst_f = open(dst_dir, 'w')
                    call(command, stdout=dst_f)
                    dst_f.close()
